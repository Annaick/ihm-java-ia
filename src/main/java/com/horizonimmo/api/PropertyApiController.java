package com.horizonimmo.api;

import com.horizonimmo.model.Property;
import com.horizonimmo.model.TransactionType;
import com.horizonimmo.service.PropertyService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recherche de biens, utilisable par l'agent vocal x.ai (function calling)
 * pour mentionner 1-2 biens correspondant au besoin pendant la conversation.
 */
@RestController
public class PropertyApiController {

    private final PropertyService propertyService;

    public PropertyApiController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/api/properties")
    public List<Property> search(
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer maxPrice) {
        return propertyService.search(TransactionType.parseLoose(transactionType), zone, propertyType, maxPrice);
    }
}
