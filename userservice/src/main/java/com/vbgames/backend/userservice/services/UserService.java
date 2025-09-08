package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.User;

@Service
public interface UserService {

    public UserResponse getUser(UUID username);

    public UserResponse registerUser(UserRequest user);

    public UserResponse updateUser(UserRequest userDto);

    public UserResponse updateFavouriteGame(UserRequest userDto);

    

}
