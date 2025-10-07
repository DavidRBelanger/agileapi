package com.dbelanger.spring.agileapi.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.Sprint;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.ProjectRepository;
import com.dbelanger.spring.agileapi.repository.SprintRepository;

@ActiveProfiles("test")
class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private SprintService sprintService;

    private User user;
    private Project project;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Organization org = new Organization();
        org.setId(1L);
        user = new User();
        user.setOrganization(org);
        project = new Project();
        project.setOrganization(org);
        project.setId(5L);
    }

    @Test
    void getSprintsByProjectId_returnsList() throws Exception {
        when(projectRepository.findById(5L)).thenReturn(Optional.of(project));
        when(sprintRepository.findAllByProjectId(5L)).thenReturn(List.of(new Sprint()));
        List<Sprint> result = sprintService.getSprintsByProjectId(5L, user);
        assertThat(result).hasSize(1);
    }

    @Test
    void createSprint_saves() throws Exception {
        when(projectRepository.findById(5L)).thenReturn(Optional.of(project));
        Sprint s = new Sprint();
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(inv -> inv.getArgument(0));
        Sprint saved = sprintService.createSprint(s, 5L, user);
        assertThat(saved.getProject()).isSameAs(project);
    }

    @Test
    void getSprintById_found() throws Exception {
        Sprint s = new Sprint();
        s.setProject(project);
        when(sprintRepository.findById(9L)).thenReturn(Optional.of(s));
        Sprint got = sprintService.getSprintById(9L, user);
        assertThat(got).isSameAs(s);
    }

    @Test
    void updateSprintById_updatesFields() throws Exception {
        Sprint s = new Sprint();
        s.setProject(project);
        when(sprintRepository.findById(9L)).thenReturn(Optional.of(s));
        when(sprintRepository.save(s)).thenReturn(s);
        Sprint patch = new Sprint();
        patch.setName("new");
        Sprint updated = sprintService.updateSprintById(9L, patch, user);
        assertThat(updated.getName()).isEqualTo("new");
    }

    @Test
    void deleteSprintById_deletes() throws Exception {
        Sprint s = new Sprint();
        s.setProject(project);
        when(sprintRepository.findById(9L)).thenReturn(Optional.of(s));
        sprintService.deleteSprintById(9L, user);
        verify(sprintRepository).delete(s);
    }
}
