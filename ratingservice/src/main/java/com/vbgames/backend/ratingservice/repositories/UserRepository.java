package com.vbgames.backend.ratingservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.ratingservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
