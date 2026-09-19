package com.horizonimmo.api;

import com.horizonimmo.service.AvailabilityService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Disponibilites de l'agence, consultables par l'agent vocal x.ai avant de
 * proposer un creneau de rendez-vous.
 */
@RestController
public class AvailabilityApiController {

    private final AvailabilityService availabilityService;

    public AvailabilityApiController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/api/disponibilites")
    public List<Map<String, Object>> get() {
        return availabilityService.findAll().stream()
                .map(a -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("jour", a.getDayOfWeek().toString());
                    row.put("ouvert", a.isActive());
                    row.put("debut", a.isActive() ? a.getStartTime().toString() : null);
                    row.put("fin", a.isActive() ? a.getEndTime().toString() : null);
                    return row;
                })
                .toList();
    }
}
