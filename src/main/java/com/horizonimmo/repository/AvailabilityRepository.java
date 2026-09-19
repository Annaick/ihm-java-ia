package com.horizonimmo.repository;

import com.horizonimmo.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
