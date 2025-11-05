package com.vbgames.backend.friendshipservice.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {
    private UUID id;
    private Boolean accepted;
    private String username;
}
