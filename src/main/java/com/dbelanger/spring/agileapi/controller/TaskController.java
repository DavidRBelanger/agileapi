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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dbelanger.spring.agileapi.dto.TaskRequest;
import com.dbelanger.spring.agileapi.model.Task;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<List<Task>> listTasks(@PathVariable long sprintId,
                                                @RequestParam(required = false) Task.Status status,
                                                @RequestParam(required = false) Integer priority,
                                                @RequestParam(required = false) Long assigneeId,
                                                Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.getTasksBySprintId(sprintId, user, status, priority, assigneeId));
    }

    @PostMapping("/sprints/{sprintId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable long sprintId,
                                           @Valid @RequestBody TaskRequest req,
                                           Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Task task = new Task();
        task.setTitle(req.title);
        task.setDescription(req.description);
        if (req.status != null) task.setStatus(req.status);
        if (req.priority != null) task.setPriority(req.priority);
        Task saved = taskService.createTask(task, sprintId, req.assigneeId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable long taskId, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.getTaskById(taskId, user));
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Task> patchTask(@PathVariable long taskId,
                                          @Valid @RequestBody TaskRequest req,
                                          Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        Task patch = new Task();
        patch.setTitle(req.title);
        patch.setDescription(req.description);
        if (req.status != null) patch.setStatus(req.status);
        if (req.priority != null) patch.setPriority(req.priority);
        if (req.assigneeId != null) {
            User assignee = new User();
            assignee.setId(req.assigneeId);
            patch.setAssignee(assignee);
        }
        return ResponseEntity.ok(taskService.updateTaskById(taskId, patch, user));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable long taskId, Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        taskService.deleteTaskById(taskId, user);
        return ResponseEntity.noContent().build();
    }
}
