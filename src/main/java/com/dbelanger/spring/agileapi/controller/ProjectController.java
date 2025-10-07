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

import com.dbelanger.spring.agileapi.dto.ProjectRequest;
import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/projects")
@Validated
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> listProjects(Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        long orgId = user.getOrganization().getId();
        return ResponseEntity.ok(projectService.getProjectsByOrganizationId(orgId, user));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest req, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Project project = new Project();
        project.setName(req.name);
        project.setDescription(req.description);
        project.setOrganization(new Organization(user.getOrganization().getId()));
        Project saved = projectService.createNewProject(project, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable long id, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(projectService.getProjectById(id, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Project> patchProject(@PathVariable long id, @Valid @RequestBody ProjectRequest req, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Project patch = new Project();
        patch.setName(req.name);
        patch.setDescription(req.description);
        return ResponseEntity.ok(projectService.updateProjectById(patch, id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        projectService.deleteProjectById(id, user);
        return ResponseEntity.noContent().build();
    }
}
