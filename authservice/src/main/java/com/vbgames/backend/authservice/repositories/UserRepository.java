package com.vbgames.backend.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.authservice.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);

    @Query(value = "DELETE FROM users WHERE expired_at < :now", nativeQuery = true)
    void deleteExpiredUsers(Long now);
}
