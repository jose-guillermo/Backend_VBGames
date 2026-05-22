package com.vbgames.backend.realtimeservice.notifications;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.common.dto.Action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveMadeNotification {
    private UUID matchId;
    private List<Action> actions;
    private boolean isGameOver;
}
