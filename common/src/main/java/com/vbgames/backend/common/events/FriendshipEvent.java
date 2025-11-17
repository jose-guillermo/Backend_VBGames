package com.vbgames.backend.common.events;

import java.util.UUID;

import com.vbgames.backend.common.enums.FriendshipEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipEvent {

    private UUID senderId;
    private UUID recipientId;
    private FriendshipEventType type;

}
