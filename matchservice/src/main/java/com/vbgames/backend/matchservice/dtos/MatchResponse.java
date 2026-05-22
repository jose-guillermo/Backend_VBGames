package com.vbgames.backend.matchservice.dtos;

import java.util.UUID;

import com.vbgames.backend.matchservice.enums.FinishReason;
import com.vbgames.backend.matchservice.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {

    private UUID id;
    private UUID player1Id;
    private UUID player2Id;
    private UUID winnerId;
    private UUID gameId;
    private long playedAt;
    private MatchStatus status;
    private FinishReason finishReason;
    private boolean asynchronous;

}
