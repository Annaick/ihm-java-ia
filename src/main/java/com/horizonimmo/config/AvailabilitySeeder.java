package com.horizonimmo.config;

import com.horizonimmo.model.Availability;
import com.horizonimmo.repository.AvailabilityRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cree les 7 lignes de disponibilite par defaut au premier demarrage :
 * lundi-samedi 9h-19h actifs, dimanche inactif (coherent avec le pied de
 * page du site vitrine).
 */
@Component
public class AvailabilitySeeder implements CommandLineRunner {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilitySeeder(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public void run(String... args) {
        if (availabilityRepository.count() == 0) {
            for (DayOfWeek day : DayOfWeek.values()) {
                Availability availability = new Availability();
                availability.setDayOfWeek(day);
                availability.setActive(day != DayOfWeek.SUNDAY);
                availability.setStartTime(LocalTime.of(9, 0));
                availability.setEndTime(LocalTime.of(19, 0));
                availabilityRepository.save(availability);
            }
        }
    }
}
