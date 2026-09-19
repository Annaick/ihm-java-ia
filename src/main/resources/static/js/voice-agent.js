/**
 * Widget de l'agent vocal x.ai (Voice Agent Builder).
 *
 * L'agent (instructions, voix, outils "API Request" vers /api/properties et
 * /api/leads) est configure directement dans le tableau de bord x.ai
 * (console.x.ai/voice/agents) — ce script ne fait que capturer/jouer
 * l'audio et relayer les octets vers notre backend, qui relaie a son tour
 * vers x.ai (voir VoiceProxyHandler.java). Aucune cle API ni logique de
 * function-calling cote navigateur.
 *
 * NOTE : les noms exacts des evenements de transcript texte ne sont pas
 * entierement documentes publiquement au moment de l'ecriture de ce
 * fichier. Le handler ci-dessous reconnait les motifs les plus probables
 * (voir handleServerEvent) ; a verifier/ajuster apres un premier test reel.
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

  async function startConversation() {
    setStatus("Connexion à l'assistant…");

    const configResp = await fetch("/api/xai/config");
    const config = await configResp.json();
    if (!config.configured) {
      setStatus("Agent vocal pas encore configuré côté serveur.");
      addMessage("assistant", "Désolé, l'agent vocal n'est pas encore activé sur ce site.");
      return;
    }

    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    playbackTime = audioContext.currentTime;

    const wsProtocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    socket = new WebSocket(wsProtocol + "//" + window.location.host + "/ws/voice");

    socket.addEventListener("open", () => {
      listening = true;
      micBtn.textContent = "";
      micBtn.innerHTML = '<i class="bi bi-stop-fill"></i> Arrêter la conversation';
      micBtn.classList.add("listening");
      statusLineEl.textContent = "En ligne · Prêt à vous écouter";
      setStatus("L'IA vous écoute…");
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

  function handleServerEvent(data) {
    const type = data.type || "";

    if (type.includes("output_audio.delta") && data.delta) {
      playAudioChunk(data.delta);
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
    micBtn.innerHTML = '<i class="bi bi-mic-fill"></i> Parler à notre conseiller';
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
