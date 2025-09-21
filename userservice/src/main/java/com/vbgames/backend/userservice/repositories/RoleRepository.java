package com.vbgames.backend.userservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vbgames.backend.userservice.entities.Role;

public interface RoleRepository extends CrudRepository<Role, UUID> {
    Optional<Role> findByName(String name);

}
