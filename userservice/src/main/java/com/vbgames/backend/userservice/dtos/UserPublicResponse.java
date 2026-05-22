package com.vbgames.backend.userservice.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicResponse {

    private UUID id;
    private String username;
    private long creationDateEpoch;
    private boolean online;
    private GameResponse favouriteGame;
}
