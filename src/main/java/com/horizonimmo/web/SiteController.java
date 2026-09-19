package com.horizonimmo.web;

import com.horizonimmo.model.TransactionType;
import com.horizonimmo.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteController {

    private final PropertyService propertyService;

    public SiteController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("properties", propertyService.findFeatured());
        return "index";
    }

    @GetMapping("/annonces")
    public String annonces(
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer maxPrice,
            Model model) {
        model.addAttribute("properties", propertyService.search(transactionType, zone, propertyType, maxPrice));
        model.addAttribute("transactionType", transactionType);
        model.addAttribute("zone", zone);
        model.addAttribute("propertyType", propertyType);
        model.addAttribute("maxPrice", maxPrice);
        return "annonces";
    }
}
