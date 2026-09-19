package com.horizonimmo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration de l'integration x.ai Voice Agent.
 *
 * La cle d'API (xai.api-key) doit etre fournie via la variable
 * d'environnement XAI_API_KEY (voir deploy/horizon-immo.env sur le
 * serveur) — jamais commitee dans le code.
 */
@ConfigurationProperties(prefix = "xai")
public class XaiProperties {

    /** Cle secrete x.ai, utilisee uniquement cote serveur. */
    private String apiKey;

    /** Modele vocal temps reel a utiliser. */
    private String model = "grok-voice-latest";

    /** Voix de l'agent. */
    private String voice = "eve";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
