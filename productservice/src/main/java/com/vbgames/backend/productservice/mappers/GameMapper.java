package com.vbgames.backend.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.GameEvent;
import com.vbgames.backend.productservice.dtos.GameResponse;
import com.vbgames.backend.productservice.entities.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "products", ignore = true)
    Game toGame(GameEvent gameEvent);

    @Mapping(target = "products", ignore = true)
    Game toGame(GameResponse gameResponse);

}
