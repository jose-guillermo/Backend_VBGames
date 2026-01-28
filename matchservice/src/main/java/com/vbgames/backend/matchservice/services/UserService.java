package com.vbgames.backend.matchservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.matchservice.entities.User;
import com.vbgames.backend.matchservice.mappers.UserMapper;
import com.vbgames.backend.matchservice.repositories.UserRepository;

import jakarta.transaction.Transactional;
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
        User user = userRepository.findById(event.getId()).get();
        user.setUsername(event.getUsername());
    }
}
