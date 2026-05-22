package com.vbgames.backend.gameservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbgames.backend.gameservice.entities.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {

    boolean existsByName(String name);
    boolean existsById(UUID id);
}
