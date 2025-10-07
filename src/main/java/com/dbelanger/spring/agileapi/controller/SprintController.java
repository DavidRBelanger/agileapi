package com.dbelanger.spring.agileapi.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbelanger.spring.agileapi.dto.SprintRequest;
import com.dbelanger.spring.agileapi.model.Sprint;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.service.SprintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/projects/{projectId}/sprints")
    public ResponseEntity<List<Sprint>> listSprints(@PathVariable long projectId, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(sprintService.getSprintsByProjectId(projectId, user));
    }

    @PostMapping("/projects/{projectId}/sprints")
    public ResponseEntity<Sprint> createSprint(@PathVariable long projectId, @Valid @RequestBody SprintRequest req, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Sprint sprint = new Sprint();
        sprint.setName(req.name);
        sprint.setStartDate(req.startDate != null ? req.startDate.atStartOfDay() : null);
        sprint.setEndDate(req.endDate != null ? req.endDate.atTime(23, 59, 59) : null);
        Sprint saved = sprintService.createSprint(sprint, projectId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/sprints/{sprintId}")
    public ResponseEntity<Sprint> getSprint(@PathVariable long sprintId, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(sprintService.getSprintById(sprintId, user));
    }

    @PatchMapping("/sprints/{sprintId}")
    public ResponseEntity<Sprint> patchSprint(@PathVariable long sprintId, @Valid @RequestBody SprintRequest req, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Sprint patch = new Sprint();
        patch.setName(req.name);
        patch.setStartDate(req.startDate != null ? req.startDate.atStartOfDay() : null);
        patch.setEndDate(req.endDate != null ? req.endDate.atTime(23, 59, 59) : null);
        return ResponseEntity.ok(sprintService.updateSprintById(sprintId, patch, user));
    }

    @DeleteMapping("/sprints/{sprintId}")
    public ResponseEntity<Void> deleteSprint(@PathVariable long sprintId, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        sprintService.deleteSprintById(sprintId, user);
        return ResponseEntity.noContent().build();
    }
}
