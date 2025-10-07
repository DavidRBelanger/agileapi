package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationService organizationService;

    public ProjectService(ProjectRepository projectRepository, OrganizationService organizationService) {
        this.projectRepository = projectRepository;
        this.organizationService = organizationService;
    }

    public List<Project> getProjectsByOrganizationId(long orgId, User user) throws AccessDeniedException {
        organizationService.assertUserInOrganization(user, new Organization(orgId));
        return projectRepository.findAllByOrganization_Id(orgId);
    }

    public Project createNewProject(Project project, User user) throws AccessDeniedException {
        organizationService.assertUserInOrganization(user, project.getOrganization());
        return projectRepository.save(project);
    }

    public Project getProjectById(long projectId, User user) throws AccessDeniedException {
        return projectRepository.findByIdAndOrganization_Id(projectId, user.getOrganization().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project at " + projectId + " not found."));
    }

    public Project updateProjectById(Project updatedProject, long id, User user) throws AccessDeniedException {
        Project project = projectRepository.findByIdAndOrganization_Id(id, user.getOrganization().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project at " + id + " not found."));

        if (updatedProject.getName() != null) {
            project.setName(updatedProject.getName());
        }

        if (updatedProject.getDescription() != null) {
            project.setDescription(updatedProject.getDescription());
        }

        return projectRepository.save(project);
    }

    public void deleteProjectById(long id, User user) throws AccessDeniedException {
        Project project = projectRepository.findByIdAndOrganization_Id(id, user.getOrganization().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project at " + id + " not found."));

        projectRepository.delete(project);
    }
}
