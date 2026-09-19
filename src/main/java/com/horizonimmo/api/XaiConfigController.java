package com.horizonimmo.api;

import com.horizonimmo.config.XaiProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Indique au widget front si l'agent vocal x.ai est configure cote serveur
 * (cle API + ID d'agent), pour afficher un message clair sinon.
 */
@RestController
public class XaiConfigController {

    private final XaiProperties xaiProperties;

    public XaiConfigController(XaiProperties xaiProperties) {
        this.xaiProperties = xaiProperties;
    }

    @GetMapping("/api/xai/config")
    public Map<String, Object> config() {
        return Map.of("configured", xaiProperties.isConfigured());
    }
}
