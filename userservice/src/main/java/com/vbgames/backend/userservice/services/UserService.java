package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.UserEventType;
import com.vbgames.backend.common.events.UpdateCoinsEvent;
import com.vbgames.backend.common.events.UserEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.userservice.dtos.RegisterRequest;
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
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest user) {
        if(userRepository.existsByEmail(user.getEmail())) 
            throw new DuplicateResourceException("El correo '" + user.getEmail() + "' ya existe en la tabla users");
        
        if(userRepository.existsByUsername(user.getUsername())) 
            throw new DuplicateResourceException("El nombre de usuario '" + user.getUsername() + "' ya existe en la tabla users");

        // Hashcodear la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());

        User newUser = userRepository.save(userMapper.toUser(user));
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        newUser.getRoles().add(userRole);

        sendUserEvent(newUser, UserEventType.CREATED);

        return userMapper.toUserResponse(newUser);
    }

    @Transactional
    public UserResponse updateUsername(String username, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setUsername(username);

        sendUserEvent(user, UserEventType.UPDATED);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateFavouriteGame(UUID userId, UUID gameId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        user.setFavouriteGame(game);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void onlineOffline(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setOnline(!user.isOnline());

    }

    @KafkaListener(topics = "user.coins.updated")
    @Transactional
    public void handleUpdateCoinsEvent(UpdateCoinsEvent updateCoinsEvent) {
        userRepository.findById(updateCoinsEvent.getId())
            .ifPresent(user -> user.setCoins(updateCoinsEvent.getCoins()));
    }

    private void sendUserEvent(User user, UserEventType type) {
        UserEvent userEvent = userMapper.toUserEvent(user, type);

        kafkaTemplate.send("user.events", userEvent);
    }
}
