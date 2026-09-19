package com.horizonimmo.api;

import com.horizonimmo.config.XaiProperties;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/**
 * Cote serveur uniquement : echange notre cle secrete XAI_API_KEY contre un
 * jeton ephemere ("client secret") que le navigateur peut utiliser pour
 * ouvrir la connexion WebSocket temps reel vers x.ai, sans jamais exposer la
 * cle API principale au client.
 *
 * Doc x.ai : POST https://api.x.ai/v1/realtime/client_secrets
 * Le navigateur utilise le token recu ici en prefixant "xai-client-secret."
 * dans le header Sec-WebSocket-Protocol lors de l'ouverture du WebSocket
 * vers wss://api.x.ai/v1/realtime?model=... (voir static/js/voice-agent.js).
 */
@RestController
public class XaiSessionController {

    private static final String CLIENT_SECRETS_URL = "https://api.x.ai/v1/realtime/client_secrets";

    private final XaiProperties xaiProperties;
    private final RestClient restClient = RestClient.create();

    public XaiSessionController(XaiProperties xaiProperties) {
        this.xaiProperties = xaiProperties;
    }

    @GetMapping("/api/xai/config")
    public Map<String, Object> config() {
        return Map.of(
                "configured", xaiProperties.isConfigured(),
                "model", xaiProperties.getModel(),
                "voice", xaiProperties.getVoice());
    }

    @PostMapping("/api/xai/session")
    public Object createSession() {
        if (!xaiProperties.isConfigured()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "XAI_API_KEY non configuree sur le serveur.");
        }

        return restClient.post()
                .uri(CLIENT_SECRETS_URL)
                .header("Authorization", "Bearer " + xaiProperties.getApiKey())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(Map.of("expires_after", Map.of("seconds", 300)))
                .retrieve()
                .body(Object.class);
    }
}
