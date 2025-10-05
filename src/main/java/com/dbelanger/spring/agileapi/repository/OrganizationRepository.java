package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findBySlug(String slug);

    Optional<Organization> findByName(String name);

    boolean existsBySlug(String slug);
}
