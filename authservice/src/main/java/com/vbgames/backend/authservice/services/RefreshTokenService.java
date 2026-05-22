package com.vbgames.backend.authservice.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.authservice.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Crea una nueva transaccion para evitar problemas con la concurrencia
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeAllByUserId(UUID token) {
        refreshTokenRepository.deleteByUserId(token);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteByExpiredTokens() {
        long now = Instant.now().toEpochMilli();

        refreshTokenRepository.deleteExpired(now);
    }
}
