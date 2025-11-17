package com.vbgames.backend.messageservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.messageservice.entities.User;
import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, UUID>{

    Optional<User> findByUsername(String username);
}
