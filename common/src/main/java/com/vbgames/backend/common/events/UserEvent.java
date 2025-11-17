package com.vbgames.backend.common.events;

import java.util.UUID;

import com.vbgames.backend.common.enums.UserEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private UUID id;
    private String username;
    private UserEventType type;
}
