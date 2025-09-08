package com.vbgames.backend.gameservice.services;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.gameservice.dtos.GameCreateRequest;
import com.vbgames.backend.gameservice.dtos.GameResponse;
import com.vbgames.backend.gameservice.dtos.GameUpdateRequest;

public interface GameService {

    public GameResponse create(GameCreateRequest gameDto);
    public List<GameResponse> getAll();
    public GameResponse update(GameUpdateRequest gameDto, UUID id); 

}
