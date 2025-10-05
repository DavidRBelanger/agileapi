package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findAllByProjectId(long projectId);

    boolean existsByNameAndProject_Id(String name, long projectId);
}

