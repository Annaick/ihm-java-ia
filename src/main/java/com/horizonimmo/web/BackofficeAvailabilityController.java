package com.horizonimmo.web;

import com.horizonimmo.service.AvailabilityService;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BackofficeAvailabilityController {

    private final AvailabilityService availabilityService;

    public BackofficeAvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/backoffice/disponibilites")
    public String view(Model model) {
        model.addAttribute("availabilities", availabilityService.findAll());
        return "backoffice/disponibilites";
    }

    @PostMapping("/backoffice/disponibilites")
    public String update(
            @RequestParam List<Long> id,
            @RequestParam(required = false) List<DayOfWeek> activeDays,
            @RequestParam List<String> startTime,
            @RequestParam List<String> endTime) {
        List<DayOfWeek> active = activeDays == null ? List.of() : activeDays;
        List<com.horizonimmo.model.Availability> availabilities = availabilityService.findAll();
        for (int i = 0; i < id.size(); i++) {
            Long targetId = id.get(i);
            com.horizonimmo.model.Availability availability = availabilities.stream()
                    .filter(a -> a.getId().equals(targetId))
                    .findFirst()
                    .orElseThrow();
            availability.setActive(active.contains(availability.getDayOfWeek()));
            availability.setStartTime(LocalTime.parse(startTime.get(i)));
            availability.setEndTime(LocalTime.parse(endTime.get(i)));
            availabilityService.save(availability);
        }
        return "redirect:/backoffice/disponibilites";
    }
}
