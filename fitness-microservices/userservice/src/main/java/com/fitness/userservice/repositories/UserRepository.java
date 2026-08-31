package com.fitness.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitness.userservice.models.User;


public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    boolean existsByKeycloakId(String keycloakId);
    User findByEmail(String email);

}
