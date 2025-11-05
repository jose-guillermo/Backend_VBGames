package com.vbgames.backend.userservice.mappers;

import org.mapstruct.Mapper;

import com.vbgames.backend.common.events.GameEvent;
import com.vbgames.backend.userservice.dtos.GameResponse;
import com.vbgames.backend.userservice.entities.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {
    
    Game toGame(GameResponse gameResponse);
    Game toGame(GameEvent gameEvent);

}
