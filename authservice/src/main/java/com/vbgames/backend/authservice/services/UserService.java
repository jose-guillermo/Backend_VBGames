package com.vbgames.backend.authservice.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.authservice.dtos.RegisterRequest;
import com.vbgames.backend.authservice.dtos.UserResponse;
import com.vbgames.backend.authservice.entities.Role;
import com.vbgames.backend.authservice.entities.User;
import com.vbgames.backend.authservice.mappers.UserMapper;
import com.vbgames.backend.authservice.repositories.RoleRepository;
import com.vbgames.backend.authservice.repositories.UserRepository;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MailService mailService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest user) {

        Optional<User> duplicatedUser = userRepository.findByEmail(user.getEmail());

        // Si el correo ya existe, pero no ha sido verificado, lo borramos
        if(duplicatedUser.isPresent()){
            User existingUser = duplicatedUser.get();
            if (existingUser.isVerified())
                throw new DuplicateResourceException("El correo '" + user.getEmail() + "' ya existe en la tabla users");
            if (existingUser.getExpiredAt() < Instant.now().toEpochMilli())
                userRepository.delete(existingUser);
            else{
                mailService.sendVerificationMail(existingUser.getUsername(), existingUser.getEmail());
                throw new IllegalStateException("Ya existe un registro pendiente de verificación.");
            }
        }

        if(userRepository.existsByUsername(user.getUsername())) 
            throw new DuplicateResourceException("El nombre de usuario '" + user.getUsername() + "' ya existe en la tabla users");

        // Hashcodear la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());

        User newUser = userRepository.save(userMapper.toUser(user));
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        newUser.getRoles().add(userRole);

        mailService.sendVerificationMail(newUser.getUsername(), newUser.getEmail());

        return userMapper.toUserResponse(newUser);
    }

    @Transactional
    public void verifyEmail(String token) {
        Claims claims = jwtService.verifyEmailToken(token);
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        User user = userRepository.findByEmail(email).get();

        sendUserEvent(user, username);

        user.setVerified(true);
        user.setExpiredAt(null);
    }

    private void sendUserEvent(User user, String username) {
        UserCreatedEvent userEvent = userMapper.toUserCreatedEvent(user, username);

        kafkaTemplate.send("user.created", userEvent);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredUsers() {

        userRepository.deleteExpiredUsers(Instant.now().toEpochMilli());
    }
}
