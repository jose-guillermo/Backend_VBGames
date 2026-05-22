package com.vbgames.backend.userservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbgames.backend.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
