package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.userservice.dtos.LoginRequest;
import com.vbgames.backend.userservice.dtos.LoginResponse;
import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.mappers.UserMapper;
import com.vbgames.backend.userservice.repositories.RoleRepository;
import com.vbgames.backend.userservice.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse registerUser(UserRequest user) {
        if(userRepository.existsByEmail(user.getEmail())) 
            throw new DuplicateResourceException("El correo '" + user.getEmail() + "' ya existe en la tabla users");
        
        if(userRepository.existsByUsername(user.getUsername())) 
            throw new DuplicateResourceException("El nombre de usuario '" + user.getUsername() + "' ya existe en la tabla users");

        // Hashcodear la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = userRepository.save(userMapper.toUser(user));
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        userMapper.addRole(newUser, userRole);

        return userMapper.toUserResponse(newUser);
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

    @Override
    @Transactional(readOnly = true)
    public LoginResponse validateCredentials(LoginRequest logingRequest) {
        User user = userRepository.findByEmail(logingRequest.getEmail()).orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrecto"));
        if(!passwordEncoder.matches(logingRequest.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Usuario o contraseña incorrecto");

        return userMapper.toLoginResponse(user);
    }

    // @Transactional
    // public User getUser(String username) {
    //     return userRepository.findByUsername(username);
    // }
}
