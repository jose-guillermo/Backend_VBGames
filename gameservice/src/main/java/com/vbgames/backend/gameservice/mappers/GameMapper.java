package com.vbgames.backend.gameservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.vbgames.backend.common.events.GameUpsertedEvent;
import com.vbgames.backend.gameservice.dtos.GameCreateRequest;
import com.vbgames.backend.gameservice.dtos.GameResponse;
import com.vbgames.backend.gameservice.dtos.GameUpdateRequest;
import com.vbgames.backend.gameservice.entities.Game;

@Mapper(componentModel = "spring", uses = {PieceMapper.class})
public interface GameMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pieces", ignore = true)
    Game GameCreateRequestToGame(GameCreateRequest gameDto);

    GameResponse toGameResponse(Game game);

    List<GameResponse> toGamesResponses(List<Game> games);

    GameUpsertedEvent toGameEvent(Game game);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pieces", ignore = true)
    void updateGame(@MappingTarget Game game, GameUpdateRequest gameDto);

}
