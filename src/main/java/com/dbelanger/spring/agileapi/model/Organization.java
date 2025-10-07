/// ============================================================
/// Class: Organization
/// Package: com.dbelanger.spring.agileapi.model
/// Project: Agile REST API
/// ============================================================
/// An `Organization` is a model representing any organization that uses this API.
/// The listed Organization is provided in the form of its `slug` in the URI, or its ID but the organization access permission is still defined by the JWT header validation.
/// Organizations have the following members:
///
/// @id: an identifier for the organization, required in the URI header for access.
/// @name: String name for the organization, in the format of "Example Company".
/// @createdAt: LocalDateTime a datetime object for when the organization was originally created.
/// @slug: String slug representation of the organization's name, in the format of "example-company":
/// Author: David Belanger
/// Created: October 3, 2025
/// Last Updated: October 3, 2025
/// ============================================================
package com.dbelanger.spring.agileapi.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Organization() {
    }

    public Organization(long id) {
        this.id = id;
    }


    public Organization(long id, String name, LocalDateTime createdAt, String slug) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.slug = slug;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
