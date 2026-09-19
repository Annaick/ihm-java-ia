package com.horizonimmo.repository;

import com.horizonimmo.model.Lead;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    List<Lead> findAllByOrderByRequestedSlotAsc();
}
