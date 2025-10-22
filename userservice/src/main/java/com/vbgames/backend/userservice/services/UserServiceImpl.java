package com.vbgames.backend.userservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.GameEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.userservice.dtos.RegisterRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Game;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.mappers.GameMapper;
import com.vbgames.backend.userservice.mappers.UserMapper;
import com.vbgames.backend.userservice.repositories.GameRepository;
import com.vbgames.backend.userservice.repositories.RoleRepository;
import com.vbgames.backend.userservice.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final GameMapper gameMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest user) {
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
    @Transactional
    public UserResponse updateUsername(String username, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setUsername(username);

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateFavouriteGame(UUID userId, UUID gameId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        user.setFavouriteGame(game);

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void onlineOffline(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setOnline(!user.isOnline());

    }

    @KafkaListener(topics = "game.events", groupId = "user-service")
    @Transactional
    public void handleGameEvent(GameEvent gameEvent) {
        try{
            System.out.println("Received game event: " + gameEvent);
            
            Game game = gameMapper.toGame(gameEvent);
            System.out.println("Game: " + game);
            gameRepository.save(game);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Transactional
    // public User getUser(String username) {
    //     return userRepository.findByUsername(username);
    // }
}
