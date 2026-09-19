package com.horizonimmo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Un lead = le resume qualifie par l'agent vocal, en attente (ou non) d'un
 * rendez-vous avec un agent humain. Cree via l'API {@code /api/leads},
 * appelee par l'agent vocal x.ai apres qualification du besoin.
 */
@Entity
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prospectName;
    private String prospectPhone;
    private String prospectEmail;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Integer budget;
    private String zone;
    private String propertyType;
    private Integer roomsWanted;
    private String timeline;

    private LocalDateTime requestedSlot;

    @Column(length = 2000)
    private String conversationSummary;

    @Enumerated(EnumType.STRING)
    private LeadStatus status = LeadStatus.NOUVEAU;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
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

    public LeadStatus getStatus() {
        return status;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
