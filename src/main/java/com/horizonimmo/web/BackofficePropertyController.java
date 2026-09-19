package com.horizonimmo.web;

import com.horizonimmo.model.Property;
import com.horizonimmo.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Gestion du catalogue de biens depuis le backoffice : le meme catalogue
 * sert le site vitrine ET l'outil "API Request" de l'agent vocal x.ai
 * (GET /api/properties), donc toute modification ici est immediatement
 * visible des deux cotes.
 */
@Controller
public class BackofficePropertyController {

    private final PropertyService propertyService;

    public BackofficePropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/backoffice/biens")
    public String list(Model model) {
        model.addAttribute("properties", propertyService.findAll());
        return "backoffice/biens";
    }

    @GetMapping("/backoffice/biens/nouveau")
    public String createForm(Model model) {
        model.addAttribute("property", new Property());
        return "backoffice/bien-form";
    }

    @GetMapping("/backoffice/biens/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.findById(id));
        return "backoffice/bien-form";
    }

    @PostMapping("/backoffice/biens")
    public String save(@ModelAttribute Property property) {
        propertyService.save(property);
        return "redirect:/backoffice/biens";
    }

    @PostMapping("/backoffice/biens/{id}/supprimer")
    public String delete(@PathVariable Long id) {
        propertyService.deleteById(id);
        return "redirect:/backoffice/biens";
    }
}
