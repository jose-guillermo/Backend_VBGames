package com.vbgames.backend.ratingservice.mappers;

import org.mapstruct.Mapper;

import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.ratingservice.dtos.GameResponse;
import com.vbgames.backend.ratingservice.entities.Game;

@Mapper(componentModel = "spring")
public interface GameMapper {

    Game toGame(GameUpsertedEvent gameEvent);

    GameResponse toGameResponse(Game game);

}