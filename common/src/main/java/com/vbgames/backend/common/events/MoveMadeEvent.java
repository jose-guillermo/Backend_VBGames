package com.vbgames.backend.common.events;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.common.dto.Action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveMadeEvent {

    private UUID userIdToSend;

    private UUID matchId;

    private List<Action> actions;

    private boolean gameOver;
}
