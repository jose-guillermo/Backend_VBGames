package com.vbgames.backend.userservice.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private int coins;
    private long creationDateEpoch;
    private boolean online;
    private GameResponse favouriteGame;
    private List<String> roles;
}
