package com.vbgames.backend.userservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbgames.backend.userservice.entities.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {

}
