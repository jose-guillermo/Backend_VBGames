package com.vbgames.backend.common.events;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayerDisconnectedEvent {
    private UUID matchId;
    private List<UUID> playerIds;
    private List<UUID> unavailablePlayerIds;
}
