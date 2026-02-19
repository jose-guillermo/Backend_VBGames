package com.vbgames.backend.realtimeservice.notifications;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusNotification {

    private UUID userId;
    private Boolean online;
}
