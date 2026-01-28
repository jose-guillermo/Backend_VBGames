package com.vbgames.backend.messageservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.messageservice.dtos.SendMessageRequest;
import com.vbgames.backend.messageservice.entities.User;
import com.vbgames.backend.messageservice.enums.MessageType;
import com.vbgames.backend.messageservice.mappers.UserMapper;
import com.vbgames.backend.messageservice.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final UserMapper userMapper;
    private UUID systemUserId;

    @PostConstruct
    private void init() {
        systemUserId = userRepository.findByUsername("system")
            .orElseThrow(() -> new IllegalStateException("System user not found"))
            .getId();
    }

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {
        User user = userMapper.toUser(event);

        User newUser = userRepository.save(user);
        SendMessageRequest request = new SendMessageRequest(
            "MESSAGES.WELCOMING_MESSAGE.TITLE",
            "MESSAGES.WELCOMING_MESSAGE.BODY",
            MessageType.SYSTEM_NOTIFICATION
        );
        
        messageService.sendMessage(this.systemUserId, newUser.getId(), request);
    }

    @KafkaListener(topics = "user.username.updated")
    @Transactional
    public void handleUsernameUpdated(UsernameUpdatedEvent event) {
        User user = userMapper.toUser(event);
        
        userRepository.save(user);
    }
}
