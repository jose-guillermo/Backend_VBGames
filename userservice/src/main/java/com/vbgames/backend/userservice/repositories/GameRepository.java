package com.vbgames.backend.userservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vbgames.backend.userservice.entities.Game;

public interface GameRepository extends CrudRepository<Game, UUID> {

}
