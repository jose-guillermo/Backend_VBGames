package com.vbgames.backend.matchservice.services;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.MoveMadeEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.matchservice.dtos.MatchResponse;
import com.vbgames.backend.matchservice.dtos.MoveRequest;
import com.vbgames.backend.matchservice.entities.Match;
import com.vbgames.backend.matchservice.entities.Move;
import com.vbgames.backend.matchservice.mappers.MatchMapper;
import com.vbgames.backend.matchservice.mappers.MoveMapper;
import com.vbgames.backend.matchservice.repositories.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MoveMapper moveMapper;
    private final KafkaTemplate<String, MoveMadeEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public MatchResponse getMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() ->new ResourceNotFoundException("Partida no encontrada", ErrorCode.MATCH_NOT_FOUND));

        return matchMapper.toMatchResponse(match);
    }

    @Transactional
    public void sendMove(MoveRequest moveRequest, UUID userId) {
        Match match = matchRepository.findById(moveRequest.getMatchId())
            .orElseThrow(() ->new ResourceNotFoundException("Partida no encontrada", ErrorCode.MATCH_NOT_FOUND));
        
        Move move = moveMapper.moveRequestToMove(moveRequest, match);

        match.getMoves().add(move);

        UUID rivalId = match.getPlayer1().getId().equals(userId) ? match.getPlayer2().getId() : match.getPlayer1().getId(); 

        System.out.println("rivalId: " + rivalId + " - userId: " + userId);

        sendMoveMadeEvent(move, rivalId);
    }

    @Transactional
    public void finishMatch(UUID matchId) {
        // TODO: Send match finished event
    }

    private void sendMoveMadeEvent(Move move, UUID userIdToSend) {
        MoveMadeEvent moveMadeEvent = moveMapper.toMoveMadeEvent(move, userIdToSend);

        System.out.println("Enviando evento: " + moveMadeEvent);
        kafkaTemplate.send("move.made", moveMadeEvent);
    }

}