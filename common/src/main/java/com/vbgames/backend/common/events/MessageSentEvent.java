package com.vbgames.backend.common.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent {

    private UUID messageId;
    private UUID recipientId;
}
