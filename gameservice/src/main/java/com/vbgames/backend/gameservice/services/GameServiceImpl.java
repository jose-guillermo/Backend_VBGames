package com.vbgames.backend.gameservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.gameservice.dtos.GameDto;
import com.vbgames.backend.gameservice.dtos.PieceDto;
import com.vbgames.backend.gameservice.entities.Game;
import com.vbgames.backend.gameservice.mappers.GameMapper;
import com.vbgames.backend.gameservice.mappers.PieceMapper;
import com.vbgames.backend.gameservice.repositories.GameRepository;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private PieceMapper pieceMapper;
    
    @Override
    @Transactional
    public GameDto create(GameDto gameDto) {
        if(gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("El nombre '" + gameDto.getName() + "' ya existe en la tabla games");

        Game game = gameRepository.save(gameMapper.toGame(gameDto));
        List<PieceDto> piecesDto = gameDto.getPieces();
        game.setPieces(pieceMapper.toPieces(piecesDto, game));
    
        return gameMapper.toGameDto(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameDto> getAll() {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return gameMapper.toGamesDto(games);
    }

    @Override
    @Transactional
    public GameDto update(GameDto gameDto, UUID id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        // Si el nombre del juego es diferente al actual y existe en la base de datos lanzamos exception
        if (!gameDto.getName().equals(game.getName()) && gameRepository.existsByName(gameDto.getName())) 
            throw new DuplicateResourceException("El nombre '" + gameDto.getName() + "' ya existe en la tabla games");

        game.setName(gameDto.getName());

        List<PieceDto> piecesDto = gameDto.getPieces();
        game.getPieces().clear();
        game.getPieces().addAll(pieceMapper.toPieces(piecesDto, game));

        game = gameRepository.save(game);
        return gameMapper.toGameDto(game);
    }
}
