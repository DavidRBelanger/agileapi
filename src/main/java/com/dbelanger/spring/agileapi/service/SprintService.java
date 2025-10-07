package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.Sprint;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.ProjectRepository;
import com.dbelanger.spring.agileapi.repository.SprintRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final OrganizationService organizationService;

    public SprintService(SprintRepository sprintRepository,
                         ProjectRepository projectRepository,
                         OrganizationService organizationService) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.organizationService = organizationService;
    }

    public List<Sprint> getSprintsByProjectId(long projectId, User user) throws AccessDeniedException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project at " + projectId + " not found."));

        organizationService.assertUserInOrganization(user, project.getOrganization());
        return sprintRepository.findAllByProjectId(projectId);
    }

    public Sprint createSprint(Sprint sprint, long projectId, User user) throws AccessDeniedException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project at " + projectId + " not found."));

        organizationService.assertUserInOrganization(user, project.getOrganization());

        sprint.setProject(project);
        return sprintRepository.save(sprint);
    }

    public Sprint getSprintById(long sprintId, User user) throws AccessDeniedException {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint at " + sprintId + " not found."));

        organizationService.assertUserInOrganization(user, sprint.getProject().getOrganization());
        return sprint;
    }

    public Sprint updateSprintById(long sprintId, Sprint updatedSprint, User user) throws AccessDeniedException {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint at " + sprintId + " not found."));

        organizationService.assertUserInOrganization(user, sprint.getProject().getOrganization());

        if (updatedSprint.getName() != null) {
            sprint.setName(updatedSprint.getName());
        }
        if (updatedSprint.getStartDate() != null) {
            sprint.setStartDate(updatedSprint.getStartDate());
        }
        if (updatedSprint.getEndDate() != null) {
            sprint.setEndDate(updatedSprint.getEndDate());
        }

        return sprintRepository.save(sprint);
    }

    public void deleteSprintById(long sprintId, User user) throws AccessDeniedException {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint at " + sprintId + " not found."));

        organizationService.assertUserInOrganization(user, sprint.getProject().getOrganization());
        sprintRepository.delete(sprint);
    }
}
