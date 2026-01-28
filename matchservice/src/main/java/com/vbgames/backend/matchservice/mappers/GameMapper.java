package com.vbgames.backend.matchservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.matchservice.entities.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "matches", ignore = true)
    Game toGame(GameUpsertedEvent gameEvent);
}
