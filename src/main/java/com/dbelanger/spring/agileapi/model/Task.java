/// ============================================================
/// Class: Task
/// Package: com.dbelanger.spring.agileapi.model
/// Project: Agile REST API
/// ============================================================
/// `id: Long` an identifier for the task.
/// `title: String` the title of the task.
/// `description: String (not required)` the description of the task, optional.
/// `status: Enum: T0_DO, IN_PROGRESS, DONE, BLOCKED` the status of the task.
/// `priority: int` the priority of the task, bounded to 1-5
/// `user: User` reference to the `User` object for this task.
/// `sprint: Sprint` reference to the `Sprint` object for this task.
/// ============================================================

package com.dbelanger.spring.agileapi.model;

import jakarta.persistence.*;

public class Task {

    public enum Status {
        TO_DO, IN_PROGRESS, DONE, BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private Status status;

    @Column
    private int priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sprint_id", nullable = false)
    private Sprint spring;

    public Task(long id, String title, String description, Status status, int priority, User user, Sprint spring) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.user = user;
        this.spring = spring;
    }

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sprint getSpring() {
        return spring;
    }

    public void setSpring(Sprint spring) {
        this.spring = spring;
    }
}
