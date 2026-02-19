package com.vbgames.backend.realtimeservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.vbgames.backend.realtimeservice.notifications.UserStatusNotification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealtimeMessagingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final FriendshipService friendshipService;

    public void notifyFriendsOnline(UUID userId) {
        List<UUID> friends = friendshipService.getFriends(userId);
        System.out.println("Amigos online: " + friends);
        UserStatusNotification notification = new UserStatusNotification(userId, true);
        friends.forEach(friend -> sendToUser(friend, "/queue/status", notification));
    }

    public void sendToUser(UUID userId, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(
            userId.toString(), 
            destination, 
            payload
        );
        System.out.println("Enviando notificación a " + userId + " en " + destination);
    }
}
