package com.vbgames.backend.ratingservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.ratingservice.entities.User;
import com.vbgames.backend.ratingservice.mappers.UserMapper;
import com.vbgames.backend.ratingservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        userRepository.save(userMapper.toUser(event));
    }

    @KafkaListener(topics = "user.username.updated")
    @Transactional
    public void handleUsernameUpdatedEvent(UsernameUpdatedEvent event) {
        userRepository.save(userMapper.toUser(event));
    }

    public User get(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public Boolean exists(UUID id) {
        Boolean exists = userRepository.existsById(id);

        if (!exists) 
            throw new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND);

        return exists;
    }
}
