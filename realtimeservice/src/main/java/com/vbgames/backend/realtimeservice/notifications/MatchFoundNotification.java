package com.vbgames.backend.realtimeservice.notifications;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchFoundNotification {

    private UUID matchId;

}
