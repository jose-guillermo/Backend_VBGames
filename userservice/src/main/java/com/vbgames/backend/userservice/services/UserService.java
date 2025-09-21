package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vbgames.backend.userservice.dtos.LoginRequest;
import com.vbgames.backend.userservice.dtos.LoginResponse;
import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;

@Service
public interface UserService {

    public UserResponse getUser(UUID username);

    public UserResponse registerUser(UserRequest user);

    public UserResponse updateUser(UserRequest userDto);

    public UserResponse updateFavouriteGame(UserRequest userDto);

    public LoginResponse validateCredentials(LoginRequest userDto);

    

}
