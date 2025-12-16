package com.vbgames.backend.authservice.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.authservice.dtos.RegisterRequest;
import com.vbgames.backend.authservice.dtos.UserResponse;
import com.vbgames.backend.authservice.entities.RefreshToken;
import com.vbgames.backend.authservice.entities.Role;
import com.vbgames.backend.authservice.entities.User;
import com.vbgames.backend.authservice.exceptions.InvalidCredentialsException;
import com.vbgames.backend.authservice.mappers.UserMapper;
import com.vbgames.backend.authservice.repositories.RefreshTokenRepository;
import com.vbgames.backend.authservice.repositories.RoleRepository;
import com.vbgames.backend.authservice.repositories.UserRepository;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MailService mailService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void login(RegisterRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) 
            throw new InvalidCredentialsException();

        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        String accessToken = jwtService.createAccessToken(user.getId().toString(), roles);
        String refreshToken = jwtService.createRefreshToken(user.getId().toString());

        ResponseCookie accessTokenCookie = jwtService.createAccessCookie(accessToken);
        ResponseCookie refreshTokenCookie = jwtService.createRefreshCookie(refreshToken);

        String refreshTokenHashed = DigestUtils.sha256Hex(refreshToken);
        user.addRefreshToken(refreshTokenHashed);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        System.out.println("refreshToken: " + refreshToken);
    }

    @Transactional
    public void refresh(String refreshToken, HttpServletResponse response) {

        Claims claims = jwtService.verifyRefreshToken(refreshToken);

        String refreshTokenHashed = DigestUtils.sha256Hex(refreshToken);

        // Verificar existencia en la BD (anti-reuse)
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshTokenHashed)
            .orElseThrow(() -> {
                UUID userId = UUID.fromString(claims.getSubject());
                refreshTokenService.revokeAllByUserId(userId);
                return new InvalidCredentialsException();
            });

        User user = refreshTokenEntity.getUser();

        List<String> roles = user.getRoles().stream().map(Role::getName).toList();

        String accessToken = jwtService.createAccessToken(user.getId().toString(), roles);
        String newRefreshToken = jwtService.createRefreshToken(user.getId().toString());

        ResponseCookie accessTokenCookie = jwtService.createAccessCookie(accessToken);
        ResponseCookie refreshTokenCookie = jwtService.createRefreshCookie(newRefreshToken);

        String newRefreshTokenHashed = DigestUtils.sha256Hex(newRefreshToken);

        user.getRefreshTokens().remove(refreshTokenEntity);
        user.addRefreshToken(newRefreshTokenHashed);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {

        Optional<User> duplicatedUser = userRepository.findByEmail(request.getEmail());

        // Si el correo ya existe, pero no ha sido verificado, lo borramos
        if(duplicatedUser.isPresent()){
            User existingUser = duplicatedUser.get();
            if (existingUser.isVerified())
                throw new DuplicateResourceException("El correo '" + request.getEmail() + "' ya existe en la tabla users");
            if (existingUser.getExpiresAt() < Instant.now().toEpochMilli())
                userRepository.delete(existingUser);
            else{
                mailService.sendVerificationMail(existingUser.getEmail());
                throw new IllegalStateException("Ya existe un registro pendiente de verificación.");
            }
        }

        // Hashcodear la contraseña
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setEmail(request.getEmail().toLowerCase());

        User newUser = userRepository.save(userMapper.toUser(request));
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        newUser.getRoles().add(userRole);

        mailService.sendVerificationMail(newUser.getEmail());

        return userMapper.toUserResponse(newUser);
    }

    @Transactional
    public void verifyEmail(String token) {
        Claims claims = jwtService.verifyEmailToken(token);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).get();

        sendUserEvent(user);

        user.setVerified(true);
        user.setExpiresAt(null);
    }

    private void sendUserEvent(User user) {
        UserCreatedEvent userEvent = userMapper.toUserCreatedEvent(user);

        kafkaTemplate.send("user.created", userEvent);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredUsers() {

        userRepository.deleteExpiredUsers(Instant.now().toEpochMilli());
    }
}
