package com.horizonimmo.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Payload envoye par l'agent vocal x.ai une fois le besoin qualifie et le
 * rendez-vous propose au prospect.
 *
 * transactionType reste une String ici (pas l'enum TransactionType
 * directement) : l'agent vocal peut envoyer une casse ou un mot different
 * ("location", "louer"...), et on ne veut pas qu'une 400 de desserialisation
 * fasse echouer toute la creation du lead pour ce detail. Voir
 * TransactionType.parseLoose(), applique dans LeadApiController.
 */
public class LeadCreateRequest {

    @NotBlank
    private String prospectName;

    @NotBlank
    private String prospectPhone;

    private String prospectEmail;
    private String transactionType;
    private Integer budget;
    private String zone;
    private String propertyType;
    private Integer roomsWanted;
    private String timeline;
    private LocalDateTime requestedSlot;
    private String conversationSummary;

    public String getProspectName() {
        return prospectName;
    }

    public void setProspectName(String prospectName) {
        this.prospectName = prospectName;
    }

    public String getProspectPhone() {
        return prospectPhone;
    }

    public void setProspectPhone(String prospectPhone) {
        this.prospectPhone = prospectPhone;
    }

    public String getProspectEmail() {
        return prospectEmail;
    }

    public void setProspectEmail(String prospectEmail) {
        this.prospectEmail = prospectEmail;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getRoomsWanted() {
        return roomsWanted;
    }

    public void setRoomsWanted(Integer roomsWanted) {
        this.roomsWanted = roomsWanted;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public LocalDateTime getRequestedSlot() {
        return requestedSlot;
    }

    public void setRequestedSlot(LocalDateTime requestedSlot) {
        this.requestedSlot = requestedSlot;
    }

    public String getConversationSummary() {
        return conversationSummary;
    }

    public void setConversationSummary(String conversationSummary) {
        this.conversationSummary = conversationSummary;
    }
}
