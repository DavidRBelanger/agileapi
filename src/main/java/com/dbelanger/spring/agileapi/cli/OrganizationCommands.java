package com.dbelanger.spring.agileapi.cli;

import java.text.Normalizer;
import java.util.Locale;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.service.OrganizationService;

@Component
@ShellComponent
public class OrganizationCommands {

    private final OrganizationService organizationService;

    public OrganizationCommands(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @ShellMethod(key = "org-create", value = "Create a new organization")
    public String create(
            @ShellOption(help = "Organization name") String name,
            @ShellOption(help = "Optional slug override", defaultValue = ShellOption.NULL) String slug) {
        String finalSlug = (slug != null && !slug.isBlank()) ? slug : toSlug(name);
        Organization created = organizationService.createOrganization(name, finalSlug);
        return "Created organization id=" + created.getId() + " name='" + created.getName() + "' slug='" + created.getSlug() + "'";
    }

    private String toSlug(String input) {
        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9-]", "");
    }
}
