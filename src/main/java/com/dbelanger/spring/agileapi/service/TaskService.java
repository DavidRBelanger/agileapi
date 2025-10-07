package com.dbelanger.spring.agileapi.service;

import com.dbelanger.spring.agileapi.model.Sprint;
import com.dbelanger.spring.agileapi.model.Task;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.repository.SprintRepository;
import com.dbelanger.spring.agileapi.repository.TaskRepository;
import com.dbelanger.spring.agileapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    public TaskService(TaskRepository taskRepository,
                       SprintRepository sprintRepository,
                       UserRepository userRepository,
                       OrganizationService organizationService) {
        this.taskRepository = taskRepository;
        this.sprintRepository = sprintRepository;
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    public List<Task> getTasksBySprintId(long sprintId, User user,
                                         Task.Status status,
                                         Integer priority,
                                         Long assigneeId) throws AccessDeniedException {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint at " + sprintId + " not found."));
        organizationService.assertUserInOrganization(user, sprint.getProject().getOrganization());

        return taskRepository.findAllBySprintId(sprintId).stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> priority == null || t.getPriority() == priority)
                .filter(t -> assigneeId == null || t.getAssignee().getId() == assigneeId)
                .toList();
    }

    public Task createTask(Task task, long sprintId, long assigneeId, User user) throws AccessDeniedException {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint at " + sprintId + " not found."));
        organizationService.assertUserInOrganization(user, sprint.getProject().getOrganization());

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("Assignee user at " + assigneeId + " not found."));
        organizationService.assertUserInOrganization(user, assignee.getOrganization());

        if (task.getStatus() == null) {
            task.setStatus(Task.Status.TO_DO);
        }
        if (task.getPriority() < 1 || task.getPriority() > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5.");
        }
        if (taskRepository.existsByTitleAndSprint_Id(task.getTitle(), sprintId)) {
            throw new IllegalArgumentException("Task title already exists in this sprint.");
        }

        task.setSprint(sprint);
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public Task getTaskById(long taskId, User user) throws AccessDeniedException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task at " + taskId + " not found."));
        organizationService.assertUserInOrganization(user, task.getSprint().getProject().getOrganization());
        return task;
    }

    public Task updateTaskById(long taskId, Task updatedTask, User user) throws AccessDeniedException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task at " + taskId + " not found."));
        organizationService.assertUserInOrganization(user, task.getSprint().getProject().getOrganization());

        if (updatedTask.getTitle() != null) {
            task.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            task.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != 0) {
            if (updatedTask.getPriority() < 1 || updatedTask.getPriority() > 5) {
                throw new IllegalArgumentException("Priority must be between 1 and 5.");
            }
            task.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getAssignee() != null && updatedTask.getAssignee().getId() != 0) {
            long newAssigneeId = updatedTask.getAssignee().getId();
            User assignee = userRepository.findById(newAssigneeId)
                    .orElseThrow(() -> new IllegalArgumentException("Assignee user at " + newAssigneeId + " not found."));
            organizationService.assertUserInOrganization(user, assignee.getOrganization());
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public void deleteTaskById(long taskId, User user) throws AccessDeniedException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task at " + taskId + " not found."));
        organizationService.assertUserInOrganization(user, task.getSprint().getProject().getOrganization());
        taskRepository.delete(task);
    }
}
