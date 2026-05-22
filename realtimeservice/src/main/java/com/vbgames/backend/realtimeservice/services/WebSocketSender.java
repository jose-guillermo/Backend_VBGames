package com.vbgames.backend.realtimeservice.services;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    public void sendToUser(UUID userId, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(
            userId.toString(),
            destination,
            payload
        );
        System.out.println("Enviando notificación a " + userId + " en " + destination);
    }

    public boolean isUserConnected(UUID userId) {
        return userRegistry.getUser(userId.toString()) != null;
    }
}
