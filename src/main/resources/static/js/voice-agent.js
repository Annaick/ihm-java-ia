/**
 * Widget "appel" de l'agent vocal x.ai (Voice Agent Builder).
 *
 * L'agent (instructions, voix, outils "API Request" vers /api/properties,
 * /api/disponibilites et /api/leads) est configure dans le tableau de bord
 * x.ai (console.x.ai/voice/agents) — ce script capture/joue l'audio et
 * relaie les octets vers notre backend (VoiceProxyHandler), qui relaie a
 * son tour vers x.ai. Aucune cle API ni logique de function-calling cote
 * navigateur.
 *
 * Detection de fin de parole geree cote client (energie du signal) plutot
 * que de dependre de la detection automatique du serveur : apres une
 * bascule "parole -> silence", on envoie explicitement
 * input_audio_buffer.commit puis response.create. C'est ce qui declenche
 * la reponse de l'agent de facon fiable.
 */
(function () {
  const REALTIME_SAMPLE_RATE = 24000;
  const SILENCE_RMS_THRESHOLD = 0.012;
  const SILENCE_DURATION_MS = 900;
  const MIN_SPEECH_DURATION_MS = 250;

  const fab = document.getElementById("voice-fab");
  const panel = document.getElementById("voice-panel");
  const closeBtn = document.getElementById("voice-close");
  const hangupBtn = document.getElementById("voice-mic");
  const statusLineEl = document.getElementById("voice-status-line");
  const timerEl = document.getElementById("voice-timer");
  const avatarRing = document.getElementById("voice-avatar-ring");

  if (!fab || !panel) {
    return;
  }

  let socket = null;
  let audioContext = null;
  let micStream = null;
  let micSourceNode = null;
  let micProcessorNode = null;
  let playbackTime = 0;
  let inCall = false;

  let isSpeaking = false;
  let speechStartedAt = 0;
  let silenceStartedAt = null;

  let callStartedAt = null;
  let timerInterval = null;

  fab.addEventListener("click", () => {
    panel.classList.add("open");
    if (!inCall) {
      startConversation().catch((err) => {
        console.error("Erreur agent vocal :", err);
        setStatus("Erreur : " + err.message);
      });
    }
  });

  closeBtn.addEventListener("click", () => {
    endCall();
  });

  hangupBtn.addEventListener("click", () => {
    endCall();
  });

  function setStatus(text) {
    statusLineEl.textContent = text;
  }

  function startTimer() {
    callStartedAt = Date.now();
    updateTimer();
    timerInterval = setInterval(updateTimer, 500);
  }

  function updateTimer() {
    const elapsed = Math.floor((Date.now() - callStartedAt) / 1000);
    const m = String(Math.floor(elapsed / 60)).padStart(2, "0");
    const s = String(elapsed % 60).padStart(2, "0");
    timerEl.textContent = m + ":" + s;
  }

  function stopTimer() {
    if (timerInterval) {
      clearInterval(timerInterval);
      timerInterval = null;
    }
  }

  async function startConversation() {
    setStatus("Connexion…");

    const configResp = await fetch("/api/xai/config");
    const config = await configResp.json();
    if (!config.configured) {
      setStatus("Agent vocal pas encore configuré.");
      return;
    }

    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    playbackTime = audioContext.currentTime;

    const wsProtocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    socket = new WebSocket(wsProtocol + "//" + window.location.host + "/ws/voice");

    socket.addEventListener("open", () => {
      inCall = true;
      setStatus("En communication");
      startTimer();
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
      endCall();
    });

    socket.addEventListener("error", () => {
      setStatus("Erreur de connexion.");
    });
  }

  function handleServerEvent(data) {
    const type = data.type || "";

    if (type.includes("output_audio.delta") && data.delta) {
      playAudioChunk(data.delta);
      return;
    }

    if (type === "response.created") {
      setStatus("L'assistant répond…");
      avatarRing.classList.add("speaking");
      return;
    }

    if (type === "response.done") {
      setStatus("En communication");
      avatarRing.classList.remove("speaking");
      return;
    }

    if (type.includes("error")) {
      console.error("Evenement d'erreur x.ai :", data);
      setStatus("Erreur pendant l'appel.");
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

          detectSpeech(input);

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

  /**
   * VAD (voice activity detection) simple base sur l'energie du signal.
   * Bascule parole -> silence prolonge => on demande explicitement une
   * reponse, plutot que de dependre d'une detection cote serveur.
   */
  function detectSpeech(samples) {
    let sumSquares = 0;
    for (let i = 0; i < samples.length; i++) {
      sumSquares += samples[i] * samples[i];
    }
    const rms = Math.sqrt(sumSquares / samples.length);
    const now = performance.now();

    if (rms > SILENCE_RMS_THRESHOLD) {
      if (!isSpeaking) {
        isSpeaking = true;
        speechStartedAt = now;
        setStatus("Je vous écoute…");
      }
      silenceStartedAt = null;
    } else if (isSpeaking) {
      if (silenceStartedAt === null) {
        silenceStartedAt = now;
      } else if (now - silenceStartedAt > SILENCE_DURATION_MS) {
        isSpeaking = false;
        silenceStartedAt = null;
        if (now - speechStartedAt > MIN_SPEECH_DURATION_MS) {
          commitAndRespond();
        }
      }
    }
  }

  function commitAndRespond() {
    if (!socket || socket.readyState !== WebSocket.OPEN) return;
    socket.send(JSON.stringify({ type: "input_audio_buffer.commit" }));
    socket.send(JSON.stringify({ type: "response.create" }));
  }

  function endCall() {
    if (!inCall && !socket) {
      panel.classList.remove("open");
      return;
    }
    inCall = false;
    stopTimer();
    setStatus("Appel terminé");

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

    setTimeout(() => panel.classList.remove("open"), 400);
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
