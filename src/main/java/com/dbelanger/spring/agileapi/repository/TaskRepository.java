package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllBySprintId(long sprintId);

    boolean existsByTitleAndSprint_Id(String title, long sprintId);
}

