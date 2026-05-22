package com.vbgames.backend.common.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchFinishedEvent {

    private UUID player1Id;
    private UUID player2Id;
    private UUID winnerId;
    private UUID gameId;

}
