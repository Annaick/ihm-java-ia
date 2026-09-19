package com.horizonimmo.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Payload envoye par l'agent vocal x.ai une fois le besoin qualifie et le
 * rendez-vous propose au prospect.
 *
 * transactionType, budget et roomsWanted restent des String ici (pas les
 * types stricts TransactionType/Integer) : l'agent vocal peut envoyer une
 * casse ou un mot different pour transactionType ("location", "louer"...),
 * ou un nombre au format decimal pour budget/roomsWanted ("70000.0" au lieu
 * de "70000"), et on ne veut pas qu'une 400 de desserialisation fasse
 * echouer toute la creation du lead pour ce detail. Voir
 * TransactionType.parseLoose() et NumberUtils.parseLooseInt(), appliques
 * dans LeadApiController.
 */
public class LeadCreateRequest {

    @NotBlank
    private String prospectName;

    @NotBlank
    private String prospectPhone;

    private String prospectEmail;
    private String transactionType;
    private String budget;
    private String zone;
    private String propertyType;
    private String roomsWanted;
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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
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

    public String getRoomsWanted() {
        return roomsWanted;
    }

    public void setRoomsWanted(String roomsWanted) {
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
