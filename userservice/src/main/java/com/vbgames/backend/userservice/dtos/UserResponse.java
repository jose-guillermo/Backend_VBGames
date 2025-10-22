package com.vbgames.backend.userservice.dtos;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.common.validators.IsUUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @IsUUID
    private UUID id;

    private String username;
    private String email;
    private int coins;
    private long creationDateEpoch;
    private boolean online;
    private GameDto favouriteGame;
    private List<String> roles;
}
