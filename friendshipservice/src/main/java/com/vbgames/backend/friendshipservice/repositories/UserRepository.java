package com.vbgames.backend.friendshipservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.friendshipservice.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

}
