package com.horizonimmo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration de l'integration x.ai Voice Agent Builder.
 *
 * L'agent (instructions, voix, outils "API Request" vers notre propre API)
 * est configure dans le tableau de bord x.ai (console.x.ai/voice/agents),
 * pas dans ce code. Ce backend se contente de relayer la connexion
 * WebSocket du navigateur vers x.ai en gardant la cle secrete cote serveur.
 *
 * xai.api-key -> variable d'environnement XAI_API_KEY
 * xai.agent-id -> variable d'environnement XAI_AGENT_ID
 * (voir deploy/horizon-immo.env sur le serveur — jamais commitees dans le code)
 */
@ConfigurationProperties(prefix = "xai")
public class XaiProperties {

    private String apiKey;
    private String agentId;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank() && agentId != null && !agentId.isBlank();
    }
}
