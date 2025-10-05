package com.dbelanger.spring.agileapi.repository;

import com.dbelanger.spring.agileapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{

    List<User> findAllByOrganizationId(long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
