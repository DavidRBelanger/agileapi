package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOrganizationId(long id);

    boolean existsByNameAndOrganization(String name, Organization organization);

}
