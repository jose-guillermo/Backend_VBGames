package com.vbgames.backend.userservice.mappers;

import org.mapstruct.Mapper;

import com.vbgames.backend.common.events.GameEvent;
import com.vbgames.backend.userservice.dtos.GameDto;
import com.vbgames.backend.userservice.entities.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {
    
    GameDto toGameDto(Game game);
    Game toGame(GameDto gameDto);
    Game toGame(GameEvent gameEvent);

}
