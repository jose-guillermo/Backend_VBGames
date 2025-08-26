package com.vbgames.backend.gameservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vbgames.backend.gameservice.entities.Game;

public interface GameRepository extends CrudRepository<Game, UUID> {

    boolean existsByName(String name);
    boolean existsById(UUID id);
}
