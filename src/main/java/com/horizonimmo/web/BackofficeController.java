package com.horizonimmo.web;

import com.horizonimmo.model.LeadStatus;
import com.horizonimmo.service.LeadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Backoffice reserve aux agents humains : liste des leads qualifies par
 * l'agent vocal et traitement (confirmation du rendez-vous).
 *
 * TODO ecole : pas d'authentification pour l'instant (hors scope du MVP),
 * a ajouter avec Spring Security avant tout usage reel.
 */
@Controller
public class BackofficeController {

    private final LeadService leadService;

    public BackofficeController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping("/backoffice")
    public String dashboard(Model model) {
        model.addAttribute("leads", leadService.findAllSortedBySlot());
        return "backoffice/dashboard";
    }

    @GetMapping("/backoffice/leads/{id}")
    public String leadDetail(@PathVariable Long id, Model model) {
        model.addAttribute("lead", leadService.findById(id));
        return "backoffice/lead-detail";
    }

    @PostMapping("/backoffice/leads/{id}/statut")
    public String updateStatus(@PathVariable Long id, @RequestParam LeadStatus status) {
        leadService.updateStatus(id, status);
        return "redirect:/backoffice/leads/" + id;
    }
}
