/// ============================================================
/// Class: Sprint
/// Package: com.dbelanger.spring.agileapi.model
/// Project: Agile REST API
/// ============================================================
/// A Sprint is a model representing a 1-4 week 'sprint' in an agile development environment.
/// `id: Long` an identifier for the sprint.
/// `name: String` the name of the sprint.
/// `startDate: LocalDateTime` a datetime object for when the sprint started or when it will start.
/// `endDate: LocalDateTime` a datetime object for when the sprint ended or when it will end.
/// `project: Project` a reference to the `Project` object.
/// ============================================================

package com.dbelanger.spring.agileapi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    public Sprint(long id, String name, LocalDateTime startDate, LocalDateTime endDate, Project project) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
    }

    public Sprint() {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
