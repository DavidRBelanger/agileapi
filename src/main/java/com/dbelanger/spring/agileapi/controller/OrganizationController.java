package com.dbelanger.spring.agileapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.service.OrganizationService;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/me")
    public ResponseEntity<Organization> getMyOrganization(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Organization org = user.getOrganization();
        // reload to ensure fresh
        Organization full = organizationService.getOrganizationById(org.getId());
        return ResponseEntity.ok(full);
    }
}
