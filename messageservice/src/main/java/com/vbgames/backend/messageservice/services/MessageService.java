package com.vbgames.backend.messageservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.FriendshipCreatedEvent;
import com.vbgames.backend.common.exceptions.ForbiddenActionException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.messageservice.dtos.MessageResponse;
import com.vbgames.backend.messageservice.dtos.SendMessageRequest;
import com.vbgames.backend.messageservice.entities.Message;
import com.vbgames.backend.messageservice.entities.User;
import com.vbgames.backend.messageservice.enums.MessageType;
import com.vbgames.backend.messageservice.mappers.MessageMapper;
import com.vbgames.backend.messageservice.repositories.MessageRepository;
import com.vbgames.backend.messageservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(UUID userId) {
        List<Message> messages = messageRepository.findAllByRecipientId(userId);
        return messageMapper.toMessageResponses(messages);
    }

    @Transactional
    public MessageResponse sendMessage(UUID senderId, UUID recipientId, SendMessageRequest request) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.SENDER_NOT_FOUND));
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.RECIPIENT_NOT_FOUND));

        Message message = messageMapper.toMessage(request, sender, recipient);

        message = messageRepository.save(message);

        return messageMapper.toMessageResponse(message);        
    }

    @Transactional
    public void readMessage(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado", ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getRecipient().getId().equals(userId)) 
            throw new ForbiddenActionException("No tienes permiso para leer este mensaje");

        message.setRead(true);
    }

    @Transactional
    public void deleteMessage(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado", ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getRecipient().getId().equals(userId)) 
            throw new ForbiddenActionException("No tienes permiso para eliminar este mensaje");

        messageRepository.delete(message);
    }

    @KafkaListener(topics = "friendship.events")
    @Transactional
    public void handleFriendshipRequestSent(FriendshipCreatedEvent event) {
        String title = "";
        String body = "";
        switch (event.getType()) {
            case REQUEST -> {
                title = "MESSAGES.FRIENDSHIP_REQUEST.TITLE";
                body = "MESSAGES.FRIENDSHIP_REQUEST.BODY";
            }
            case ACCEPTED -> {
                title = "MESSAGES.FRIENDSHIP_ACCEPTED.TITLE";
                body = "MESSAGES.FRIENDSHIP_ACCEPTED.BODY";
            }
        }
        SendMessageRequest request = new SendMessageRequest(title, body, MessageType.FRIENDSHIP_REQUEST);
        sendMessage(event.getSenderId(), event.getRecipientId(), request);
    }
}
