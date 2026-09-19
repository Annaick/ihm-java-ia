package com.horizonimmo.api;

import com.horizonimmo.api.dto.LeadCreateRequest;
import com.horizonimmo.model.Lead;
import com.horizonimmo.model.TransactionType;
import com.horizonimmo.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Point d'entree appele par l'agent vocal x.ai une fois qu'il a qualifie le
 * besoin du prospect et propose un rendez-vous. Le lead cree apparait
 * immediatement dans le backoffice.
 */
@RestController
public class LeadApiController {

    private final LeadService leadService;

    public LeadApiController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/api/leads")
    @ResponseStatus(HttpStatus.CREATED)
    public Lead createLead(@Valid @RequestBody LeadCreateRequest request) {
        Lead lead = new Lead();
        lead.setProspectName(request.getProspectName());
        lead.setProspectPhone(request.getProspectPhone());
        lead.setProspectEmail(request.getProspectEmail());
        lead.setTransactionType(TransactionType.parseLoose(request.getTransactionType()));
        lead.setBudget(request.getBudget());
        lead.setZone(request.getZone());
        lead.setPropertyType(request.getPropertyType());
        lead.setRoomsWanted(request.getRoomsWanted());
        lead.setTimeline(request.getTimeline());
        lead.setRequestedSlot(request.getRequestedSlot());
        lead.setConversationSummary(request.getConversationSummary());
        return leadService.create(lead);
    }
}
