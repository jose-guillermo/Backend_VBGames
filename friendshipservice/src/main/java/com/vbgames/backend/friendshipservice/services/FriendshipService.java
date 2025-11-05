package com.vbgames.backend.friendshipservice.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public ArrayList<FriendResponse> getFriends(UUID userId) {
        return new ArrayList<FriendResponse>(friendshipRepository.findAllFriendsByUserId(userId));
    }

    @Transactional
    public void addFriendship(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if(user.equals(friend)) throw new SelfFriendRequestException("No puedes ser tu propio amigo");

        if(friendshipRepository.existsByUsers(userId, friendId)) throw new DuplicateFriendshipException("Amistad ya existente");

        user.getFriends().add(friend);
    }

    @Transactional
    public void removeFriendship(UUID userId, UUID friendId) {
        int deleted = friendshipRepository.deleteByUserAndFriend(userId, friendId);

        if (deleted == 0) {
            throw new ResourceNotFoundException("Amistad no encontrada");
        }
    }

    @Transactional
    public void acceptFriendship(UUID userId, UUID friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(friendId, userId).orElseThrow(() -> new ResourceNotFoundException("Amistad no encontrada"));
        friendship.setAccepted(true);
    }

}
