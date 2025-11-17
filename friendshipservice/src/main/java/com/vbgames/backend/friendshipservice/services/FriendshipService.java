package com.vbgames.backend.friendshipservice.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.FriendshipEventType;
import com.vbgames.backend.common.events.FriendshipEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.friendshipservice.dtos.FriendResponse;
import com.vbgames.backend.friendshipservice.entities.Friendship;
import com.vbgames.backend.friendshipservice.entities.User;
import com.vbgames.backend.friendshipservice.exceptions.DuplicateFriendshipException;
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
    public void sendFrienshipRequest(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if(user.equals(friend)) throw new SelfFriendRequestException("No puedes ser tu propio amigo");

        if(friendshipRepository.existsByUsers(userId, friendId)) throw new DuplicateFriendshipException("Amistad ya existente");

        sendFriendshipEvent(userId, friendId, FriendshipEventType.REQUEST);

        Friendship friendship = new Friendship(user, friend);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void removeFriendship(UUID userId, UUID friendId) {
        int deleted = friendshipRepository.deleteByUserIdAndFriendId(userId, friendId);

        if (deleted == 0) {
            throw new ResourceNotFoundException("Amistad no encontrada");
        }
    }

    @Transactional
    public void acceptFriendship(UUID userId, UUID friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(friendId, userId).orElseThrow(() -> new ResourceNotFoundException("Amistad no encontrada"));

        sendFriendshipEvent(userId, friendId, FriendshipEventType.ACCEPTED);
        friendship.setAccepted(true);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredFriendships() {
        long thirstyDaysAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);

        friendshipRepository.deleteExpiredFriendships(thirstyDaysAgo);
    }

    private void sendFriendshipEvent(UUID senderId, UUID recipientId, FriendshipEventType type) {
        FriendshipEvent event = new FriendshipEvent(senderId, recipientId, type);
        kafkaTemplate.send("friendship.events", event);
    }
}
