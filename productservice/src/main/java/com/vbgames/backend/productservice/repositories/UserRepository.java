package com.vbgames.backend.productservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.productservice.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findById(UUID id);
}
