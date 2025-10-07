package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    public Organization getOrganizationById(long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Organization at " + id + " not found."));
    }


    public Organization getOrganizationBySlug(String slug) {
        return organizationRepository.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("Organization at" + slug + " not found."));

    }

    public boolean slugExists(String slug) {
        return organizationRepository.existsBySlug(slug);
    }

    public boolean isUserInOrganization(User user, Organization organization) {
        return user.getOrganization().getId() == organization.getId();
    }

    public void assertUserInOrganization(User user, Organization organization) throws AccessDeniedException {
        if (!(user.getOrganization().getId() == (organization.getId()))) {
            throw new AccessDeniedException("User is not a member of organization.");
        }
    }

}
