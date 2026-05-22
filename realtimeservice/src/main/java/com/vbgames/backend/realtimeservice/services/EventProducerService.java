package com.vbgames.backend.realtimeservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vbgames.backend.common.events.MatchPlayerDisconnectedEvent;
import com.vbgames.backend.common.events.UserConnectedEvent;
import com.vbgames.backend.common.events.UserDisconnectedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserConnected(String userId) {
        UserConnectedEvent event = new UserConnectedEvent(UUID.fromString(userId));
        kafkaTemplate.send("user.connected", event);
    }

    public void sendUserDisconnected(String userId) {
        UserDisconnectedEvent event = new UserDisconnectedEvent(UUID.fromString(userId));
        kafkaTemplate.send("user.disconnected", event);
    }

    public void sendMatchPlayerDisconnected(UUID matchId, List<UUID> playerIds, List<UUID> unavailableplayerIds) {
        MatchPlayerDisconnectedEvent event = new MatchPlayerDisconnectedEvent(matchId, playerIds, unavailableplayerIds);
        kafkaTemplate.send("match.player.disconnected", event);
    }
}