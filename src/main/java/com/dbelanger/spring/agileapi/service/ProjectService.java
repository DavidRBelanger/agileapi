package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dbelanger.spring.agileapi.model.Project;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getProjectsByOrganizationId(long id) {
        return projectRepository.findAllByOrganizationId(id);
    }

    public Project createNewProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project at " + id + " not found."));
    }

    public Project updateProjectById(Project updatedProject, long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project at " + id + " not found."));

        if (updatedProject.getName() != null) {
            project.setName(updatedProject.getName());
        }

        if (updatedProject.getDescription() != null) {
            project.setDescription(updatedProject.getDescription());
        }

        return projectRepository.save(project);
    }

    public void deleteProjectById(long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project at " + id + " not found."));
        projectRepository.deleteById(id);
    }
}
