package com.horizonimmo.service;

import com.horizonimmo.model.Property;
import com.horizonimmo.model.TransactionType;
import com.horizonimmo.repository.PropertyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> findFeatured() {
        return propertyRepository.findAll().stream().limit(3).toList();
    }

    public List<Property> search(TransactionType transactionType, String zone, String propertyType, Integer maxPrice) {
        return propertyRepository.findAll().stream()
                .filter(p -> transactionType == null || p.getTransactionType() == transactionType)
                .filter(p -> zone == null || zone.isBlank() || p.getZone().equalsIgnoreCase(zone))
                .filter(p -> propertyType == null || propertyType.isBlank() || p.getPropertyType().equalsIgnoreCase(propertyType))
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .toList();
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).orElseThrow();
    }

    public List<Property> findSimilar(Property property) {
        return propertyRepository.findAll().stream()
                .filter(p -> !p.getId().equals(property.getId()))
                .filter(p -> p.getTransactionType() == property.getTransactionType())
                .limit(3)
                .toList();
    }

    public List<String> distinctPropertyTypes() {
        return propertyRepository.findAll().stream()
                .map(Property::getPropertyType)
                .distinct()
                .sorted()
                .toList();
    }
}
