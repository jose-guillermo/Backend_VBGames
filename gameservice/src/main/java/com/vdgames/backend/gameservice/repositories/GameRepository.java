package com.vdgames.backend.gameservice.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vdgames.backend.gameservice.entities.Game;

public interface GameRepository extends CrudRepository<Game, String> {

    boolean existsByName(String name);

}
