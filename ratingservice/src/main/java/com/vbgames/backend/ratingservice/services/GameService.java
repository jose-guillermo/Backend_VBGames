package com.vbgames.backend.ratingservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.ratingservice.entities.Game;
import com.vbgames.backend.ratingservice.mappers.GameMapper;
import com.vbgames.backend.ratingservice.repositories.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @KafkaListener(topics = "game.upserted")
    @Transactional
    public void handleGameUpsertedEvent (GameUpsertedEvent event) {
        Game game = gameMapper.toGame(event);
        gameRepository.save(game);
    }

    public Game get(UUID id) {
        return gameRepository.findById(id).orElse(null);
    }

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Boolean exists(UUID id) {
        Boolean exists = gameRepository.existsById(id);

        if (!exists) 
            throw new ResourceNotFoundException("Juego no encontrado", ErrorCode.GAME_NOT_FOUND);

        return exists;
    }
}
