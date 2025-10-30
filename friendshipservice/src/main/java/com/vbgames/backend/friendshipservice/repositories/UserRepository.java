package com.vbgames.backend.friendshipservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vbgames.backend.friendshipservice.entities.User;

public interface UserRepository extends CrudRepository<User, UUID> {

}
