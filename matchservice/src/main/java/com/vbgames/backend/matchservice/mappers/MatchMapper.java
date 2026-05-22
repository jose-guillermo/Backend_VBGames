package com.vbgames.backend.matchservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.MatchFoundEvent;
import com.vbgames.backend.matchservice.dtos.MatchResponse;
import com.vbgames.backend.matchservice.entities.Match;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "matchId", source = "id")
    @Mapping(target = "player1Id", source = "player1.id")
    @Mapping(target = "player2Id", source = "player2.id")
    MatchFoundEvent toMatchFoundEvent(Match match);

    @Mapping(target = "player1Id", source = "player1.id")
    @Mapping(target = "player2Id", source = "player2.id")
    @Mapping(target = "winnerId", source = "winner.id")
    @Mapping(target = "gameId", source = "game.id")
    MatchResponse toMatchResponse(Match match);
}
