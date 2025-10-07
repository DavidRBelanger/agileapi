/// ============================================================
/// Class: Project
/// Package: com.dbelanger.spring.agileapi.model
/// Project: Agile REST API
/// ============================================================
/// A project is a model representing an Agile Project. Projects are per organization.
/// `id: Long` an identifier for the project.
/// `name: String` the name of the project.
/// `description: String (not required)` the description of the project, optional.
/// `organization: Organization` a reference to the `Organization` object.
/// ============================================================

package com.dbelanger.spring.agileapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, unique = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public Project() {
    }

    public Project(long id, String name, String description, Organization organization) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organization = organization;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
