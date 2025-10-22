package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vbgames.backend.userservice.dtos.RegisterRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;

@Service
public interface UserService {

    public UserResponse getUser(UUID userId);

    public UserResponse registerUser(RegisterRequest user);

    public UserResponse updateUsername(String username, UUID userId);

    public UserResponse updateFavouriteGame(UUID userId, UUID gameId);

    public void onlineOffline(UUID userId);
}
