package com.vbgames.backend.friendshipservice.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.enums.FriendshipEventType;
import com.vbgames.backend.common.events.FriendshipEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.friendshipservice.dtos.FriendResponse;
import com.vbgames.backend.friendshipservice.entities.Friendship;
import com.vbgames.backend.friendshipservice.entities.User;
import com.vbgames.backend.friendshipservice.exceptions.SelfFriendRequestException;
import com.vbgames.backend.friendshipservice.repositories.FriendshipRepository;
import com.vbgames.backend.friendshipservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final KafkaTemplate<String, FriendshipEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public ArrayList<FriendResponse> getFriends(UUID userId) {
        return new ArrayList<FriendResponse>(friendshipRepository.findAllFriendsByUserId(userId));
    }

    @Transactional
    public void sendFriendshipRequest(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.FRIEND_NOT_FOUND));

        if(user.equals(friend)) 
            throw new SelfFriendRequestException("No puedes ser tu propio amigo");

        if(friendshipRepository.existsByUsers(userId, friendId)) 
            throw new DuplicateResourceException("Amistad ya existente", ErrorCode.FRIENDSHIP_ALREADY_EXISTS);

        Friendship friendship = new Friendship(user, friend);
        friendshipRepository.save(friendship);

        sendFriendshipEvent(userId, friendId, FriendshipEventType.REQUESTED);
    }

    @Transactional
    public void removeFriendship(UUID userId, UUID friendId) {
        Friendship friendship = friendshipRepository
            .findBetweenUsers(userId, friendId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Amistad no encontrada",
                    ErrorCode.FRIENDSHIP_NOT_FOUND)
            );

        boolean wasAccepted = friendship.isAccepted();

        friendshipRepository.delete(friendship);

        if (!wasAccepted) {
            sendFriendshipEvent(userId, friendId, FriendshipEventType.REMOVED);
        }
    }

    @Transactional
    public void acceptFriendship(UUID userId, UUID friendId) {
        Friendship friendship = friendshipRepository.findBetweenUsers(friendId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Amistad no encontrada", ErrorCode.FRIENDSHIP_NOT_FOUND));

        friendship.setAccepted(true);
        sendFriendshipEvent(userId, friendId, FriendshipEventType.ACCEPTED);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredFriendships() {
        long thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS).toEpochMilli();

        friendshipRepository.deleteExpiredFriendships(thirtyDaysAgo);
    }

    private void sendFriendshipEvent(UUID senderId, UUID recipientId, FriendshipEventType type) {
        FriendshipEvent event = new FriendshipEvent(senderId, recipientId, type);
        kafkaTemplate.send("friendship.events", event);
    }
}
