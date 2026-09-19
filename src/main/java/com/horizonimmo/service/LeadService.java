package com.horizonimmo.service;

import com.horizonimmo.model.Lead;
import com.horizonimmo.model.LeadStatus;
import com.horizonimmo.repository.LeadRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public Lead create(Lead lead) {
        lead.setStatus(LeadStatus.NOUVEAU);
        return leadRepository.save(lead);
    }

    public List<Lead> findAllSortedBySlot() {
        return leadRepository.findAllByOrderByRequestedSlotAsc();
    }

    public Lead findById(Long id) {
        return leadRepository.findById(id).orElseThrow();
    }

    public Lead updateStatus(Long id, LeadStatus status) {
        Lead lead = findById(id);
        lead.setStatus(status);
        return leadRepository.save(lead);
    }
}
