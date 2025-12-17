package com.vbgames.backend.gameservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.gameservice.dtos.GameCreateRequest;
import com.vbgames.backend.gameservice.dtos.GameResponse;
import com.vbgames.backend.gameservice.dtos.GameUpdateRequest;
import com.vbgames.backend.gameservice.entities.Game;
import com.vbgames.backend.gameservice.entities.Piece;
import com.vbgames.backend.gameservice.mappers.GameMapper;
import com.vbgames.backend.gameservice.mappers.PieceMapper;
import com.vbgames.backend.gameservice.repositories.GameRepository;
import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService{

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final PieceMapper pieceMapper;
    private final KafkaTemplate<String, GameUpsertedEvent> kafkaTemplate;

    @Transactional
    public GameResponse create(GameCreateRequest gameDto) {
        if(gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("Ya existe un juego con ese nombre", ErrorCode.GAME_ALREADY_EXISTS);

        Game game = gameRepository.save(gameMapper.GameCreateRequestToGame(gameDto));

        List<Piece> pieces = pieceMapper.toPieces(gameDto.getPieces(), game);

        game.setPieces(pieces);
    
        sendGameEvent(game);
        return gameMapper.toGameResponse(game);
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getAll() {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return gameMapper.toGamesResponses(games);
    }

    @Transactional
    public GameResponse update(GameUpdateRequest gameDto, UUID id) {
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado", ErrorCode.GAME_NOT_FOUND));

        // Si el nombre del juego es diferente al actual y existe en la base de datos lanzamos exception
        if (!gameDto.getName().equals(game.getName()) && gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("Ya existe un juego con ese nombre", ErrorCode.GAME_ALREADY_EXISTS);

        // Lanza el evento si el nombre del juego va a cambiar
        if(!gameDto.getName().equals(game.getName())) {
            gameMapper.updateGame(game, gameDto);
            sendGameEvent(game);
        }

        pieceMapper.updatePieces(game, gameDto); 

        return gameMapper.toGameResponse(game);
    }

    private void sendGameEvent(Game game) {
        GameUpsertedEvent gameEvent = gameMapper.toGameEvent(game);

        System.out.println("Sending game event: " + gameEvent);
        kafkaTemplate.send("game.upserted", gameEvent);
    }
}
