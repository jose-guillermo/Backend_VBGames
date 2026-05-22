package com.vbgames.backend.productservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.productservice.entities.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

}
