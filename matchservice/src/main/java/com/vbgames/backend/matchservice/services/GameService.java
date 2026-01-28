package com.vbgames.backend.matchservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.matchservice.entities.Game;
import com.vbgames.backend.matchservice.mappers.GameMapper;
import com.vbgames.backend.matchservice.repositories.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    
    private final GameMapper gameMapper;

    @KafkaListener(topics = "game.upserted")
    @Transactional
    public void handleGameUpserted(GameUpsertedEvent event) {
        Game game = gameMapper.toGame(event);
        gameRepository.save(game);
    }
    
}
