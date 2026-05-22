package com.vbgames.backend.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.authservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    
    @Query(value = "DELETE FROM users WHERE expires_at < :now", nativeQuery = true)
    void deleteExpiredUsers(Long now);

    @Modifying
    @Query(value = 
        "INSERT INTO users (id, email, password, verified, expires_at) VALUES (:id, :email, :password, :verified, :expiresAt)", 
        nativeQuery = true
    )
    void insertAdmin(UUID id, String email, String password, boolean verified, Long expiresAt);
}
