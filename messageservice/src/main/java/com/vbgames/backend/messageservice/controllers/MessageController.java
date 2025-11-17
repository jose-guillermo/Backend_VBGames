package com.vbgames.backend.messageservice.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.messageservice.dtos.MessageResponse;
import com.vbgames.backend.messageservice.dtos.SendMessageRequest;
import com.vbgames.backend.messageservice.services.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public List<MessageResponse> getMessages(@RequestHeader("X-User-Id") UUID userId) {
        return messageService.getMessages(userId);
    }

    @PostMapping("/{recipientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse sendMessage(
        @Valid @RequestBody SendMessageRequest request, 
        BindingResult result,
        @RequestHeader("X-User-Id") UUID senderId,
        @PathVariable @IsUUID String recipientId
    ) {
        validation(result);
        
        return messageService.sendMessage(senderId, UUID.fromString(recipientId), request);
    }

    @PatchMapping("/{messageId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readMessage(@RequestHeader("X-User-Id") UUID userId, @PathVariable @IsUUID String messageId) {
        messageService.readMessage(userId, UUID.fromString(messageId));
    }

    @DeleteMapping("/{messageId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@RequestHeader("X-User-Id") UUID userId, @PathVariable @IsUUID String messageId) {
        messageService.deleteMessage(userId, UUID.fromString(messageId));
    }

    private void validation(BindingResult result) {
        if (!result.hasFieldErrors()) return;

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        throw new RequestValidationException(errors);
    }


}
