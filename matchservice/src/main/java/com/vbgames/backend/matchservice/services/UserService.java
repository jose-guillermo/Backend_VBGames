package com.vbgames.backend.matchservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.matchservice.entities.User;
import com.vbgames.backend.matchservice.mappers.UserMapper;
import com.vbgames.backend.matchservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {
        User user = userMapper.toUser(event);
        userRepository.save(user);
    }

    @KafkaListener(topics = "user.username.updated")
    @Transactional
    public void handleUsernameUpdated(UsernameUpdatedEvent event) {
        User user = userRepository.getReferenceById(event.getId());
        user.setUsername(event.getUsername());
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        System.out.println(id);
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User getReferenceById(UUID id) {
        return userRepository.getReferenceById(id);
    }
}
