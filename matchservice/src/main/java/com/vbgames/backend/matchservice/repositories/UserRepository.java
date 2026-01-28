package com.vbgames.backend.matchservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.matchservice.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

}
