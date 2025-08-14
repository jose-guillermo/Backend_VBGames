package com.vdgames.backend.gameservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vdgames.backend.gameservice.dto.GameDto;
import com.vdgames.backend.gameservice.entities.Game;
import com.vdgames.backend.gameservice.mappers.GameMapper;
import com.vdgames.backend.gameservice.repositories.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;
    
    @Override
    public GameDto createGame(GameDto gameDto) {
        Game gameSaved = gameRepository.save(gameMapper.toGame(gameDto));
        return gameMapper.toGameDto(gameSaved);
    }
    @Override
    public List<GameDto> getAll() {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return gameMapper.toGamesDto(games);
    }

}
