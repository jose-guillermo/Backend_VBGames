package com.vbgames.backend.userservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.userservice.entities.Game;
import com.vbgames.backend.userservice.mappers.GameMapper;
import com.vbgames.backend.userservice.repositories.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameMapper gameMapper;
    private final GameRepository gameRepository;

    @KafkaListener(topics = "game.upserted")
    @Transactional
    public void handleGameUpserted(GameUpsertedEvent event) {
        Game game = gameMapper.toGame(event);

        gameRepository.save(game);
    }
}
