package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Retrieves a singular organization by its ID.
     * @param id The ID of the organization to be found.
     * @return The Organization entity
     * @throws IllegalArgumentException if not found.
     */
    public Organization getOrganizationById(long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organization at " + id + " not found."));
    }

    /**
     * Retrieves a singular organization by its slug.
     * @param slug The slug of the organization to be found.
     * @return The Organization entity
     * @throws IllegalArgumentException if not found.
     */
    public Organization getOrganizationBySlug(String slug) {
        return organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Organization at" + slug + " not found."));
        
    }

    /**
     * Returns if the organization exists at its given slug.
     * @param slug The slug of the organization to be checked.
     * @return if the organization exists.
     *
     * This is primarily useful for validation of registration and uniqueness.
     */
    public boolean slugExists(String slug) {
        return organizationRepository.existsBySlug(slug);
    }
}
