package com.vbgames.backend.gameservice.services;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.gameservice.dtos.GameDto;

public interface GameService {

    public GameDto create(GameDto gameDto);
    public List<GameDto> getAll();
    public GameDto update(GameDto gameDto, UUID id); 

}
