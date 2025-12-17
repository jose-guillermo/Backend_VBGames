package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.UserCoinsUpdatedEvent;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Game;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.mappers.UserMapper;
import com.vbgames.backend.userservice.repositories.GameRepository;
import com.vbgames.backend.userservice.repositories.RoleRepository;
import com.vbgames.backend.userservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UsernameUpdatedEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUsername(String username, UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        user.setUsername(username);

        sendUserEvent(user);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateFavouriteGame(UUID userId, UUID gameId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado", ErrorCode.GAME_NOT_FOUND));

        user.setFavouriteGame(game);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void onlineOffline(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        user.setOnline(!user.isOnline());
    }

    @KafkaListener(topics = "user.coins.updated")
    @Transactional
    public void handleUpdateCoinsEvent(UserCoinsUpdatedEvent event) {
        userRepository.findById(event.getId())
            .ifPresent(user -> user.setCoins(event.getCoins()));
    }

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        User user = userMapper.toUser(event);
        user = userRepository.save(user);

        Role userRole = roleRepository.findByName("ROLE_USER").get();

        user.getRoles().add(userRole);
    }

    private void sendUserEvent(User user) {
        UsernameUpdatedEvent event = userMapper.toUsernameUpdatedEvent(user);

        kafkaTemplate.send("username.updated", event);
    }
}
