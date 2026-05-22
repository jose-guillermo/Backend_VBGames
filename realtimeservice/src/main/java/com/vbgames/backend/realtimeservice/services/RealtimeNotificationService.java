package com.vbgames.backend.realtimeservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vbgames.backend.common.dto.Action;
import com.vbgames.backend.realtimeservice.notifications.MatchFoundNotification;
import com.vbgames.backend.realtimeservice.notifications.MoveMadeNotification;
import com.vbgames.backend.realtimeservice.notifications.UserStatusNotification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealtimeNotificationService {

    private final EventProducerService eventProducerService;
    private final FriendshipService friendshipService;
    private final WebSocketSender webSocketSender;

    public void notifyMessageReceived(UUID recipientId, UUID messageId) {
        webSocketSender.sendToUser(recipientId, "/queue/messages", messageId);
    }
    public void notifyFriendsOnline(UUID userId) {
        List<UUID> friends = friendshipService.getFriends(userId);
        System.out.println("Amigos online: " + friends);
        UserStatusNotification notification = new UserStatusNotification(userId, true);

        friends.forEach(friend -> webSocketSender.sendToUser(friend, "/queue/status", notification));
    }

    public void notifyMatchFound(UUID matchId, UUID player1Id, UUID player2Id) {
        Boolean player1Available = webSocketSender.isUserConnected(player1Id);
        Boolean player2Available = webSocketSender.isUserConnected(player2Id);
        System.out.println("Jugador 1 conectado: " + player1Available + " - Jugador 2 conectado: " + player2Available);
        System.out.println("jugador 1: " + player1Id + " - jugador 2: " + player2Id);
        
        if (player1Available && player2Available) {
            System.out.println("Enviando notificación");
            webSocketSender.sendToUser(player1Id, "/queue/match", new MatchFoundNotification(matchId));
            webSocketSender.sendToUser(player2Id, "/queue/match", new MatchFoundNotification(matchId));
        } else {
            List<UUID> playerIds = List.of(player1Id, player2Id);
            List<UUID> playerDisconnectedIds = new ArrayList<>();
            
            if (!player1Available)
                playerDisconnectedIds.add(player1Id);

            if (!player2Available)
                playerDisconnectedIds.add(player2Id);

            eventProducerService.sendMatchPlayerDisconnected(matchId, playerIds, playerDisconnectedIds);
        }
    }

    public void notifyMoveMade(UUID userIdToSend, UUID matchId, List<Action> actions, boolean gameOver) {
        MoveMadeNotification notification = new MoveMadeNotification(matchId, actions, gameOver);
        webSocketSender.sendToUser(userIdToSend, "/queue/move", notification);
    }
}
