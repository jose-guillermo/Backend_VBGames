package com.vdgames.backend.gameservice.services;

import java.util.List;

import com.vdgames.backend.gameservice.dto.GameDto;

public interface GameService {

    public GameDto createGame(GameDto gameDto);
    public List<GameDto> getAll(); 
}
