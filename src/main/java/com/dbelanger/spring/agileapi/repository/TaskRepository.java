package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllBySprintId(long sprintId);

    List<Task> findAllBySprintIdAndStatus(long sprintId, Task.Status status);

    List<Task> findAllBySprintIdAndPriority(long sprintId, int priority);

    List<Task> findAllBySprintIdAndAssignee_Id(long sprintId, long assigneeId);

    List<Task> findAllBySprintIdAndStatusAndPriority(long sprintId, Task.Status status, int priority);

    List<Task> findAllBySprintIdAndStatusAndAssignee_Id(long sprintId, Task.Status status, long assigneeId);

    List<Task> findAllBySprintIdAndPriorityAndAssignee_Id(long sprintId, int priority, long assigneeId);

    List<Task> findAllBySprintIdAndStatusAndPriorityAndAssignee_Id(
            long sprintId, Task.Status status, int priority, long assigneeId
    );

    boolean existsByTitleAndSprint_Id(String title, long sprintId);

    Optional<Task> findByIdAndSprint_Project_Organization_Id(long taskId, long organizationId);
}
