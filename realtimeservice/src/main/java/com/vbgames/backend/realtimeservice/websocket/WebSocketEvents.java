package com.vbgames.backend.realtimeservice.websocket;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.vbgames.backend.realtimeservice.services.EventProducerService;
import com.vbgames.backend.realtimeservice.services.RealtimeNotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEvents {

    private final EventProducerService eventProducerService;
    private final RealtimeNotificationService realtimeNotificationService;

    @EventListener
    public void handleConnect(SessionConnectEvent  event) {
        String userId = getUserId(event);
        System.out.println("User connected: " + userId);
        eventProducerService.sendUserConnected(userId);
        realtimeNotificationService.notifyFriendsOnline(UUID.fromString(userId));
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String userId = getUserId(event);
        System.out.println("User disconnected: " + userId);

        eventProducerService.sendUserDisconnected(userId);
    }

    private String getUserId(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        return (String) accessor.getSessionAttributes().get("userId");
    }
}