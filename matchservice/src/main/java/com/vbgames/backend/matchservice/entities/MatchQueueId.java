package com.vbgames.backend.matchservice.entities;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchQueueId implements Serializable{

    private UUID gameId;
    private UUID userId;

}
