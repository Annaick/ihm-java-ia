/**
 * Widget de l'agent vocal x.ai (Grok Voice Agent API).
 *
 * Protocole (compatible OpenAI Realtime, verifie sur docs.x.ai) :
 *  - jeton ephemere : POST /api/xai/session (notre backend) proxy vers
 *    https://api.x.ai/v1/realtime/client_secrets
 *  - WebSocket : wss://api.x.ai/v1/realtime?model=... avec le jeton passe
 *    en Sec-WebSocket-Protocol, prefixe "xai-client-secret."
 *  - function calling : evenement serveur "response.function_call_arguments.done"
 *    -> on repond avec "conversation.item.create" (function_call_output) puis
 *    "response.create".
 *
 * NOTE : les noms exacts des evenements de transcript texte (utilisateur /
 * assistant) ne sont pas entierement documentes publiquement au moment de
 * l'ecriture de ce fichier. Le handler ci-dessous reconnait les motifs les
 * plus probables (voir handleServerEvent) ; a verifier/ajuster une fois la
 * cle XAI_API_KEY branchee et un premier appel reel observe (onglet Reseau).
 */
(function () {
  const REALTIME_SAMPLE_RATE = 24000;

  const fab = document.getElementById("voice-fab");
  const panel = document.getElementById("voice-panel");
  const closeBtn = document.getElementById("voice-close");
  const micBtn = document.getElementById("voice-mic");
  const messagesEl = document.getElementById("voice-messages");
  const statusEl = document.getElementById("voice-status");
  const statusLineEl = document.getElementById("voice-status-line");

  if (!fab || !panel) {
    return;
  }

  let socket = null;
  let audioContext = null;
  let micStream = null;
  let micSourceNode = null;
  let micProcessorNode = null;
  let playbackTime = 0;
  let listening = false;
  let currentAssistantBubble = null;
  let currentUserBubble = null;

  fab.addEventListener("click", () => {
    panel.classList.add("open");
  });

  closeBtn.addEventListener("click", () => {
    panel.classList.remove("open");
  });

  micBtn.addEventListener("click", () => {
    if (listening) {
      stopConversation();
    } else {
      startConversation().catch((err) => {
        console.error("Erreur agent vocal :", err);
        setStatus("Erreur : " + err.message);
      });
    }
  });

  function setStatus(text) {
    statusEl.textContent = text;
  }

  function addMessage(role, text) {
    const div = document.createElement("div");
    div.className = "voice-message " + (role === "user" ? "user" : "assistant");
    div.textContent = text;
    messagesEl.appendChild(div);
    messagesEl.scrollTop = messagesEl.scrollHeight;
    return div;
  }

  function addRdvCard(details) {
    const card = document.createElement("div");
    card.className = "voice-rdv-card";
    card.innerHTML =
      '<div class="label"><i class="bi bi-calendar2-check-fill"></i> Rendez-vous enregistré</div>' +
      '<div><strong>' + escapeHtml(details.requestedSlot || "Créneau à confirmer") + "</strong></div>" +
      '<div>' + escapeHtml(details.summary || "") + "</div>";
    messagesEl.appendChild(card);
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function escapeHtml(str) {
    const div = document.createElement("div");
    div.textContent = str == null ? "" : String(str);
    return div.innerHTML;
  }

  async function startConversation() {
    setStatus("Connexion à l'assistant…");

    const configResp = await fetch("/api/xai/config");
    const config = await configResp.json();
    if (!config.configured) {
      setStatus("Agent vocal pas encore configuré (clé x.ai manquante côté serveur).");
      addMessage("assistant", "Désolé, l'agent vocal n'est pas encore activé sur ce site.");
      return;
    }

    const sessionResp = await fetch("/api/xai/session", { method: "POST" });
    if (!sessionResp.ok) {
      throw new Error("Impossible d'obtenir un jeton de session (" + sessionResp.status + ")");
    }
    const sessionData = await sessionResp.json();
    const ephemeralToken = extractToken(sessionData);
    if (!ephemeralToken) {
      throw new Error("Jeton de session introuvable dans la réponse serveur.");
    }

    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    playbackTime = audioContext.currentTime;

    const wsUrl = "wss://api.x.ai/v1/realtime?model=" + encodeURIComponent(config.model);
    socket = new WebSocket(wsUrl, ["xai-client-secret." + ephemeralToken]);

    socket.addEventListener("open", () => {
      listening = true;
      micBtn.textContent = "⏹️ Arrêter la conversation";
      micBtn.classList.add("listening");
      statusLineEl.textContent = "En ligne · Prêt à vous écouter";
      setStatus("L'IA vous écoute…");

      socket.send(
        JSON.stringify({
          type: "session.update",
          session: {
            voice: config.voice,
            instructions: buildInstructions(),
            turn_detection: { type: "server_vad" },
            tools: buildTools(),
          },
        })
      );

      startMicCapture();
    });

    socket.addEventListener("message", (event) => {
      let data;
      try {
        data = JSON.parse(event.data);
      } catch (e) {
        return;
      }
      handleServerEvent(data);
    });

    socket.addEventListener("close", () => {
      stopConversation();
    });

    socket.addEventListener("error", () => {
      setStatus("Erreur de connexion à l'agent vocal.");
    });
  }

  function extractToken(sessionData) {
    if (!sessionData) return null;
    if (typeof sessionData === "string") return sessionData;
    if (sessionData.value) return sessionData.value;
    if (sessionData.client_secret && sessionData.client_secret.value) {
      return sessionData.client_secret.value;
    }
    if (sessionData.token) return sessionData.token;
    return null;
  }

  function buildInstructions() {
    return (
      "Tu es le conseiller vocal d'Horizon Immo, une agence immobilière. " +
      "Tu es chaleureux, professionnel et concis. Ton but n'est PAS de tout expliquer : " +
      "c'est de qualifier le besoin du visiteur puis d'obtenir un rendez-vous avec un agent humain. " +
      "Pose des questions courtes, une par une : achat ou location, budget, zone/quartier, type de bien, " +
      "nombre de pièces, délai du projet. Tu peux utiliser l'outil rechercher_biens pour mentionner 1 ou 2 " +
      "biens correspondants si c'est pertinent. Une fois le besoin qualifié, propose un rendez-vous : " +
      "demande un créneau souhaité, le nom, le téléphone et l'email du prospect, puis appelle l'outil " +
      "creer_rendez_vous avec toutes ces informations et un résumé de la conversation. Confirme oralement " +
      "le rendez-vous une fois l'outil appelé. Réponds toujours en français."
    );
  }

  function buildTools() {
    return [
      {
        type: "function",
        name: "rechercher_biens",
        description: "Recherche des biens immobiliers correspondant a des criteres, pour les mentionner au prospect.",
        parameters: {
          type: "object",
          properties: {
            transactionType: { type: "string", enum: ["VENTE", "LOCATION"], description: "Achat ou location" },
            zone: { type: "string", description: "Quartier ou zone recherchee" },
            propertyType: { type: "string", description: "Type de bien (APPARTEMENT, MAISON, STUDIO...)" },
            maxPrice: { type: "number", description: "Budget maximum" },
          },
          required: [],
        },
      },
      {
        type: "function",
        name: "creer_rendez_vous",
        description: "Enregistre le lead qualifie et le rendez-vous propose dans le backoffice de l'agence.",
        parameters: {
          type: "object",
          properties: {
            prospectName: { type: "string" },
            prospectPhone: { type: "string" },
            prospectEmail: { type: "string" },
            transactionType: { type: "string", enum: ["VENTE", "LOCATION"] },
            budget: { type: "number" },
            zone: { type: "string" },
            propertyType: { type: "string" },
            roomsWanted: { type: "number" },
            timeline: { type: "string", description: "Delai du projet, ex: '3 mois'" },
            requestedSlot: { type: "string", description: "Date et heure ISO 8601 du creneau souhaite" },
            conversationSummary: { type: "string", description: "Resume de la conversation pour l'agent humain" },
          },
          required: ["prospectName", "prospectPhone", "requestedSlot"],
        },
      },
    ];
  }

  async function handleServerEvent(data) {
    const type = data.type || "";

    if (type.includes("output_audio.delta") && data.delta) {
      playAudioChunk(data.delta);
      return;
    }

    if (type.includes("function_call_arguments.done")) {
      await handleFunctionCall(data);
      return;
    }

    // Transcript best-effort (noms d'evenements a confirmer avec un test reel) :
    if (type.includes("transcript") && type.includes("delta") && typeof data.delta === "string") {
      const isUser = type.includes("input_audio_transcription");
      if (isUser) {
        if (!currentUserBubble) currentUserBubble = addMessage("user", "");
        currentUserBubble.textContent += data.delta;
      } else {
        if (!currentAssistantBubble) currentAssistantBubble = addMessage("assistant", "");
        currentAssistantBubble.textContent += data.delta;
      }
      return;
    }

    if (type.includes("transcript") && type.includes("completed")) {
      currentAssistantBubble = null;
      currentUserBubble = null;
      return;
    }

    if (type.includes("error")) {
      console.error("Evenement d'erreur x.ai :", data);
      setStatus("L'assistant a rencontré une erreur.");
    }
  }

  async function handleFunctionCall(data) {
    const name = data.name;
    const callId = data.call_id;
    let args = {};
    try {
      args = JSON.parse(data.arguments || "{}");
    } catch (e) {
      args = {};
    }

    let result;
    try {
      if (name === "rechercher_biens") {
        result = await callSearchProperties(args);
      } else if (name === "creer_rendez_vous") {
        result = await callCreateLead(args);
        addRdvCard({
          requestedSlot: args.requestedSlot,
          summary: args.conversationSummary,
        });
      } else {
        result = { error: "Fonction inconnue: " + name };
      }
    } catch (e) {
      result = { error: e.message };
    }

    socket.send(
      JSON.stringify({
        type: "conversation.item.create",
        item: {
          type: "function_call_output",
          call_id: callId,
          output: JSON.stringify(result),
        },
      })
    );
    socket.send(JSON.stringify({ type: "response.create" }));
  }

  async function callSearchProperties(args) {
    const params = new URLSearchParams();
    if (args.transactionType) params.set("transactionType", args.transactionType);
    if (args.zone) params.set("zone", args.zone);
    if (args.propertyType) params.set("propertyType", args.propertyType);
    if (args.maxPrice) params.set("maxPrice", args.maxPrice);
    const resp = await fetch("/api/properties?" + params.toString());
    return await resp.json();
  }

  async function callCreateLead(args) {
    const resp = await fetch("/api/leads", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(args),
    });
    return await resp.json();
  }

  function startMicCapture() {
    navigator.mediaDevices
      .getUserMedia({ audio: true })
      .then((stream) => {
        micStream = stream;
        micSourceNode = audioContext.createMediaStreamSource(stream);
        micProcessorNode = audioContext.createScriptProcessor(4096, 1, 1);

        micProcessorNode.onaudioprocess = (event) => {
          if (!socket || socket.readyState !== WebSocket.OPEN) return;
          const input = event.inputBuffer.getChannelData(0);
          const resampled = resampleTo24k(input, audioContext.sampleRate);
          const pcm16 = floatTo16BitPCM(resampled);
          const base64Audio = arrayBufferToBase64(pcm16.buffer);
          socket.send(JSON.stringify({ type: "input_audio_buffer.append", audio: base64Audio }));
        };

        micSourceNode.connect(micProcessorNode);
        micProcessorNode.connect(audioContext.destination);
      })
      .catch((err) => {
        setStatus("Micro refusé : " + err.message);
      });
  }

  function stopConversation() {
    listening = false;
    micBtn.textContent = "🎙️ Parler à notre conseiller";
    micBtn.classList.remove("listening");
    statusLineEl.textContent = "Hors ligne";
    setStatus("Prêt à vous écouter");

    if (micProcessorNode) {
      micProcessorNode.disconnect();
      micProcessorNode = null;
    }
    if (micSourceNode) {
      micSourceNode.disconnect();
      micSourceNode = null;
    }
    if (micStream) {
      micStream.getTracks().forEach((t) => t.stop());
      micStream = null;
    }
    if (socket) {
      try {
        socket.close();
      } catch (e) {
        /* ignore */
      }
      socket = null;
    }
  }

  // --- Utilitaires audio (PCM16 / 24kHz, format attendu par l'API temps reel) ---

  function resampleTo24k(input, inputSampleRate) {
    if (inputSampleRate === REALTIME_SAMPLE_RATE) return input;
    const ratio = inputSampleRate / REALTIME_SAMPLE_RATE;
    const newLength = Math.round(input.length / ratio);
    const result = new Float32Array(newLength);
    for (let i = 0; i < newLength; i++) {
      const srcIndex = i * ratio;
      const i0 = Math.floor(srcIndex);
      const i1 = Math.min(i0 + 1, input.length - 1);
      const frac = srcIndex - i0;
      result[i] = input[i0] * (1 - frac) + input[i1] * frac;
    }
    return result;
  }

  function floatTo16BitPCM(float32Array) {
    const output = new Int16Array(float32Array.length);
    for (let i = 0; i < float32Array.length; i++) {
      const s = Math.max(-1, Math.min(1, float32Array[i]));
      output[i] = s < 0 ? s * 0x8000 : s * 0x7fff;
    }
    return output;
  }

  function arrayBufferToBase64(buffer) {
    let binary = "";
    const bytes = new Uint8Array(buffer);
    for (let i = 0; i < bytes.byteLength; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }

  function playAudioChunk(base64Audio) {
    const binary = window.atob(base64Audio);
    const bytes = new Uint8Array(binary.length);
    for (let i = 0; i < binary.length; i++) {
      bytes[i] = binary.charCodeAt(i);
    }
    const pcm16 = new Int16Array(bytes.buffer);
    const float32 = new Float32Array(pcm16.length);
    for (let i = 0; i < pcm16.length; i++) {
      float32[i] = pcm16[i] / 0x8000;
    }

    const buffer = audioContext.createBuffer(1, float32.length, REALTIME_SAMPLE_RATE);
    buffer.copyToChannel(float32, 0);

    const source = audioContext.createBufferSource();
    source.buffer = buffer;
    source.connect(audioContext.destination);

    const now = audioContext.currentTime;
    const startAt = Math.max(now, playbackTime);
    source.start(startAt);
    playbackTime = startAt + buffer.duration;
  }
})();
