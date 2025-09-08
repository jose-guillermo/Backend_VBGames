package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public UserResponse registerUser(UserRequest user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerUser'");
    }

    @Override
    public UserResponse updateUser(UserRequest userDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public UserResponse updateFavouriteGame(UserRequest userDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFavouriteGame'");
    }

    // @Transactional
    // public User getUser(String username) {
    //     return userRepository.findByUsername(username);
    // }

}
