package com.horizonimmo.service;

import com.horizonimmo.model.Availability;
import com.horizonimmo.repository.AvailabilityRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public List<Availability> findAll() {
        // DayOfWeek est stocke en texte (EnumType.STRING) : trier en base
        // donnerait un ordre alphabetique, pas l'ordre du calendrier.
        return availabilityRepository.findAll().stream()
                .sorted(Comparator.comparing(Availability::getDayOfWeek))
                .toList();
    }

    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }
}
