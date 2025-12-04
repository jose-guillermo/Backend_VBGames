package com.vbgames.backend.common.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinsUpdatedEvent {
    private UUID id;
    private int coins;
}
