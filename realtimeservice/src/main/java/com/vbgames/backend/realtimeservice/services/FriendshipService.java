package com.vbgames.backend.realtimeservice.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.FriendshipEvent;
import com.vbgames.backend.realtimeservice.entities.Friendship;
import com.vbgames.backend.realtimeservice.mappers.FriendshipMapper;
import com.vbgames.backend.realtimeservice.repositories.FriendshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;

    @Transactional(readOnly = true)
    public ArrayList<UUID> getFriends(UUID userId) {
        return new ArrayList<UUID>(friendshipRepository.findAllFriendsByUserId(userId));
    }

    @KafkaListener(topics = "friendship.events")
    @Transactional
    public void handleFriendshipEvent(FriendshipEvent event) {

        Friendship friendship = friendshipMapper.toFriendship(event);

        switch (event.getType()) {
            case ACCEPTED -> {
                if(!friendshipRepository.existsById(friendship.getId()))
                    friendshipRepository.save(friendship);
            }
            case REMOVED -> friendshipRepository.deleteById(friendship.getId());
            case REQUESTED -> {}
        }
    }
}
