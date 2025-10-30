package com.vbgames.backend.friendshipservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.UserEvent;
import com.vbgames.backend.friendshipservice.entities.User;
import com.vbgames.backend.friendshipservice.mappers.UserMapper;
import com.vbgames.backend.friendshipservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @KafkaListener(topics = "user.events", groupId = "friendship-service")
    @Transactional
    public void handleUserEvent(UserEvent userEvent) {
        User user = userMapper.toUser(userEvent);
        
        userRepository.save(user);
    }
}
