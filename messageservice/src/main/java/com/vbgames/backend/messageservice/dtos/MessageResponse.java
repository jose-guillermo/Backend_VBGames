package com.vbgames.backend.messageservice.dtos;

import java.util.UUID;

import com.vbgames.backend.messageservice.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private UUID id;
    private UserResponse sender;
    private UserResponse recipient;
    private boolean isRead;
    private String title;
    private String body;
    private MessageType type;
    private long sendDate;

}
