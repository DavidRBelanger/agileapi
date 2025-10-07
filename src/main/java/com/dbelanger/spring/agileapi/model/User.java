/// ============================================================
/// Class: User
/// Package: com.dbelanger.spring.agileapi.model
/// Project: Agile REST API
/// ============================================================
/// A user is a model representing a person user. Users are per `Organization`, so one individual person may have multiple user accounts.
/// `id: Long` an identifier for the user.
/// `email: String` a user's email.
/// `passwordHash: String` a user's encrypted password.
/// `name: String` a user's name.
/// `organization: Organization` a reference to the `Organization` object.
/// ============================================================

package com.dbelanger.spring.agileapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    //this connects users to organizations.
    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public User(long id, String email, String passwordHash, String name, Organization organization) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.organization = organization;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}
