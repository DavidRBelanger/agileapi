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
import com.dbelanger.spring.agileapi.model.Task;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.SprintRepository;
import com.dbelanger.spring.agileapi.repository.TaskRepository;
import com.dbelanger.spring.agileapi.repository.UserRepository;

@ActiveProfiles("test")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private SprintRepository sprintRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Sprint sprint;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Organization org = new Organization();
        org.setId(1L);
        user = new User();
        user.setOrganization(org);
        Project project = new Project();
        project.setOrganization(org);
        sprint = new Sprint();
        sprint.setId(3L);
        sprint.setProject(project);
    }

    @Test
    void getTasks_filters() throws Exception {
        when(sprintRepository.findById(3L)).thenReturn(Optional.of(sprint));
        Task t1 = new Task(); t1.setStatus(Task.Status.TO_DO); t1.setPriority(3); t1.setAssignee(user);
        Task t2 = new Task(); t2.setStatus(Task.Status.IN_PROGRESS); t2.setPriority(5); t2.setAssignee(user);
        when(taskRepository.findAllBySprintId(3L)).thenReturn(List.of(t1, t2));
        List<Task> filtered = taskService.getTasksBySprintId(3L, user, Task.Status.TO_DO, null, null);
        assertThat(filtered).containsExactly(t1);
    }

    @Test
    void createTask_success() throws Exception {
        when(sprintRepository.findById(3L)).thenReturn(Optional.of(sprint));
        User assignee = new User();
        assignee.setId(8L);
        Organization org = user.getOrganization();
        assignee.setOrganization(org);
        when(userRepository.findById(8L)).thenReturn(Optional.of(assignee));
        Task task = new Task();
        task.setTitle("T1");
        task.setPriority(2);
        when(taskRepository.existsByTitleAndSprint_Id("T1", 3L)).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));
        Task saved = taskService.createTask(task, 3L, 8L, user);
        assertThat(saved.getAssignee()).isEqualTo(assignee);
        assertThat(saved.getSprint()).isEqualTo(sprint);
    }

    @Test
    void updateTask_updatesFields() throws Exception {
        Task existing = new Task();
        existing.setSprint(sprint);
        existing.setPriority(2);
        when(taskRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);
        Task patch = new Task();
        patch.setTitle("new");
        patch.setPriority(4);
        User newAssignee = new User(); newAssignee.setId(9L); newAssignee.setOrganization(user.getOrganization());
        patch.setAssignee(newAssignee);
        when(userRepository.findById(9L)).thenReturn(Optional.of(newAssignee));
        Task updated = taskService.updateTaskById(11L, patch, user);
        assertThat(updated.getTitle()).isEqualTo("new");
        assertThat(updated.getPriority()).isEqualTo(4);
        assertThat(updated.getAssignee()).isEqualTo(newAssignee);
    }

    @Test
    void deleteTask_deletes() throws Exception {
        Task existing = new Task(); existing.setSprint(sprint);
        when(taskRepository.findById(11L)).thenReturn(Optional.of(existing));
        taskService.deleteTaskById(11L, user);
        verify(taskRepository).delete(existing);
    }
}
