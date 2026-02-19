package com.vbgames.backend.realtimeservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vbgames.backend.common.events.MessageSentEvent;
import com.vbgames.backend.common.events.UserStatusChangedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RealtimeMessagingService realtimeMessagingService;

    public void sendUserStatusChanged(String userId, Boolean online) {
        UserStatusChangedEvent event = new UserStatusChangedEvent(UUID.fromString(userId), online);

        kafkaTemplate.send("user.status.changed", event);
    }

    @KafkaListener(topics = "message.sent")
    public void handleMessageSent(MessageSentEvent event) {
        realtimeMessagingService.sendToUser(event.getRecipientId(), "/queue/messages", event.getMessageId());
    }
}
