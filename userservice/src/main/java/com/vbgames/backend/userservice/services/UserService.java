package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.ProductPurchasedEvent;
import com.vbgames.backend.common.events.UserCoinsUpdatedEvent;
import com.vbgames.backend.common.events.UserConnectedEvent;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UserDisconnectedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.userservice.dtos.UserPrivateResponse;
import com.vbgames.backend.userservice.dtos.UserPublicResponse;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(readOnly = true)
    public UserPublicResponse getUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserPublicResponse(user);
    }

    @Transactional(readOnly = true)
    public UserPrivateResponse getMyUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserPrivateResponse(user);
    }

    @Transactional
    public UserPublicResponse updateUsername(String username, UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        
        if (username.equals(user.getUsername()))
            return userMapper.toUserPublicResponse(user);

        userRepository.findByUsername(username)
            .ifPresent(u -> {
                throw new DuplicateResourceException(
                    "Ya existe un usuario con ese nombre de usuario",
                    ErrorCode.USERNAME_ALREADY_EXISTS
                );
            });
           
        user.setUsername(username);

        sendUserUpdated(user);

        return userMapper.toUserPublicResponse(user);
    }

    @Transactional
    public UserPublicResponse updateFavouriteGame(UUID userId, UUID gameId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado", ErrorCode.GAME_NOT_FOUND));
        
        user.setFavouriteGame(game);

        return userMapper.toUserPublicResponse(user);
    }

    @KafkaListener(topics = "product.purchased")
    @Transactional
    public void handleProductPurchased(ProductPurchasedEvent event) {
        User user = userRepository.getReferenceById(event.getUserId());
        
        user.setCoins(user.getCoins() - event.getPrice());
        
        sendUserCoinsUpdated(user);
    }

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {
        User user = userMapper.toUser(event);
        user = userRepository.save(user);

        Role userRole = roleRepository.findByName("ROLE_USER").get();

        user.getRoles().add(userRole);
    }

    @KafkaListener(topics = "user.connected")
    @Transactional
    public void handleUserConnected(UserConnectedEvent event) {
        User user = userRepository.getReferenceById(event.getId());

        user.setOnline(true);
    }

    @KafkaListener(topics = "user.diconnected")
    @Transactional
    public void handleUserDisconnected(UserDisconnectedEvent event) {
        User user = userRepository.getReferenceById(event.getId());

        user.setOnline(false);
    }

    private void sendUserUpdated(User user) {
        UsernameUpdatedEvent event = userMapper.toUsernameUpdatedEvent(user);

        kafkaTemplate.send("user.username.updated", event);
    }

    private void sendUserCoinsUpdated(User user) {
        UserCoinsUpdatedEvent event = userMapper.toUserCoinsUpdatedEvent(user);

        kafkaTemplate.send("user.coins.updated", event);
    }
}
