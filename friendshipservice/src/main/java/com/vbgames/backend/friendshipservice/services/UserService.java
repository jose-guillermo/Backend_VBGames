package com.vbgames.backend.friendshipservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.friendshipservice.entities.User;
import com.vbgames.backend.friendshipservice.mappers.UserMapper;
import com.vbgames.backend.friendshipservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserEvent(UserCreatedEvent event) {
        User user = userMapper.toUser(event);
        
        userRepository.save(user);
    }

    @KafkaListener(topics = "username.updated")
    @Transactional
    public void handleUserEvent(UsernameUpdatedEvent event) {
        User user = userMapper.toUser(event);
        
        userRepository.save(user);
    }
}
