package com.vbgames.backend.realtimeservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.FriendshipEvent;
import com.vbgames.backend.common.events.MatchFoundEvent;
import com.vbgames.backend.common.events.MessageSentEvent;
import com.vbgames.backend.common.events.MoveMadeEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventConsumerService {

    private final FriendshipService friendshipService;
    private final RealtimeNotificationService realtimeNotificationService;

    @KafkaListener(topics = "message.sent")
    public void handleMessageSent(MessageSentEvent event) {
        realtimeNotificationService.notifyMessageReceived(event.getRecipientId(), event.getMessageId());
    }

    @KafkaListener(topics = "friendship.events")
    @Transactional
    public void handleFriendshipEvent(FriendshipEvent event) {
        friendshipService.updateFrienship(event);
    }

    @KafkaListener(topics = "match.found")
    public void handleMatchFoundEvent(MatchFoundEvent event) {
        realtimeNotificationService.notifyMatchFound(event.getMatchId(), event.getPlayer1Id(), event.getPlayer2Id());
    }

    @KafkaListener(topics = "move.made")
    public void handleMoveMadeEvent(MoveMadeEvent event) {
        realtimeNotificationService.notifyMoveMade(event.getUserIdToSend(), event.getMatchId(), event.getActions(), event.isGameOver());
    }
}