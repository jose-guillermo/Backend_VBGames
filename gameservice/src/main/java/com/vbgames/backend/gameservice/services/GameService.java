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
import com.vbgames.backend.common.events.GameEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService{

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final PieceMapper pieceMapper;
    private final KafkaTemplate<String, GameEvent> kafkaTemplate;

    @Transactional
    public GameResponse create(GameCreateRequest gameDto) {
        if(gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("El nombre '" + gameDto.getName() + "' ya existe en la tabla games");

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
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        // Si el nombre del juego es diferente al actual y existe en la base de datos lanzamos exception
        if (!gameDto.getName().equals(game.getName()) && gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("El nombre '" + gameDto.getName() + "' ya existe en la tabla games");

        // Lanza el evento si el nombre del juego va a cambiar
        if(!gameDto.getName().equals(game.getName())) {
            gameMapper.updateGame(game, gameDto);
            sendGameEvent(game);
        }

        pieceMapper.updatePieces(game, gameDto); 

        return gameMapper.toGameResponse(game);
    }

    private void sendGameEvent(Game game) {
        GameEvent gameEvent = gameMapper.toGameEvent(game);

        System.out.println("Sending game event: " + gameEvent);
        kafkaTemplate.send("game.events", gameEvent);
    }
}
