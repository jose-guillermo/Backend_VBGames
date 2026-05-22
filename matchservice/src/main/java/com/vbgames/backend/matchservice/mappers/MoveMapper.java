package com.vbgames.backend.matchservice.mappers;

import java.util.UUID;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vbgames.backend.common.events.MoveMadeEvent;
import com.vbgames.backend.matchservice.dtos.MoveRequest;
import com.vbgames.backend.matchservice.entities.Match;
import com.vbgames.backend.matchservice.entities.Move;
import com.vbgames.backend.matchservice.entities.MoveId;

import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring")
public interface MoveMapper {
    ObjectMapper objectMapper = new ObjectMapper(); 

    @Mapping(target = "id", source = ".", qualifiedByName = "toMoveId")
    @Mapping(target = "match", expression = "java(match)")
    Move moveRequestToMove(MoveRequest moveRequest, @Context Match match);

    @Mapping(target = "userIdToSend", source = "userId")
    @Mapping(target = "matchId", source = "move.id.matchId")
    MoveMadeEvent toMoveMadeEvent(Move move, UUID userId);
    
    @Named("toMoveId")
    default MoveId toMoveId(MoveRequest moveRequest, @Context Match match) {
        MoveId id = new MoveId();
        id.setMatchId(moveRequest.getMatchId());
        id.setTurn(moveRequest.getTurn());
        return id;
    }
}
