package com.dbelanger.spring.agileapi.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.ProjectRepository;

@ActiveProfiles("test")
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private ProjectService projectService;

    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Organization org = new Organization();
        org.setId(1L);
        user = new User();
        user.setId(10L);
        user.setOrganization(org);
    }

    @Test
    @DisplayName("getProjectsByOrganizationId returns list when authorized")
    void getProjects() throws Exception {
        Project p = new Project();
        when(projectRepository.findAllByOrganization_Id(1L)).thenReturn(List.of(p));
        List<Project> result = projectService.getProjectsByOrganizationId(1L, user);
        assertThat(result).hasSize(1);
        verify(organizationService).assertUserInOrganization(eq(user), any(Organization.class));
    }

    @Test
    @DisplayName("createNewProject saves when authorized")
    void createProject() throws Exception {
        Project p = new Project();
        Organization org = new Organization();
        org.setId(1L);
        p.setOrganization(org);
        when(projectRepository.save(p)).thenReturn(p);
        Project saved = projectService.createNewProject(p, user);
        assertThat(saved).isSameAs(p);
    }

    @Test
    void getProjectById_found() throws Exception {
        Project p = new Project();
        Organization org = new Organization();
        org.setId(1L);
        p.setOrganization(org);
        when(projectRepository.findByIdAndOrganization_Id(5L, 1L)).thenReturn(Optional.of(p));
        Project got = projectService.getProjectById(5L, user);
        assertThat(got).isSameAs(p);
    }

    @Test
    void getProjectById_notFound() {
        when(projectRepository.findByIdAndOrganization_Id(5L, 1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> projectService.getProjectById(5L, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateProjectById_appliesPatch() throws Exception {
        Project existing = new Project();
        Organization org = new Organization();
        org.setId(1L);
        existing.setOrganization(org);
        existing.setName("old");
        existing.setDescription("oldD");
        Project patch = new Project();
        patch.setName("new");
        patch.setDescription("newD");
        when(projectRepository.findByIdAndOrganization_Id(7L, 1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);
        Project updated = projectService.updateProjectById(patch, 7L, user);
        assertThat(updated.getName()).isEqualTo("new");
        assertThat(updated.getDescription()).isEqualTo("newD");
    }

    @Test
    void deleteProjectById_deletes() throws Exception {
        Project existing = new Project();
        Organization org = new Organization();
        org.setId(1L);
        existing.setOrganization(org);
        when(projectRepository.findByIdAndOrganization_Id(9L, 1L)).thenReturn(Optional.of(existing));
        projectService.deleteProjectById(9L, user);
        verify(projectRepository).delete(existing);
    }
}
