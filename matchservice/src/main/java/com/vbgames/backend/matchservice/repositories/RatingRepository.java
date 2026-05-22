package com.vbgames.backend.matchservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.matchservice.entities.Rating;
import com.vbgames.backend.matchservice.entities.RatingId;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    Optional<Rating> findByUserIdAndGameId(UUID userId, UUID gameId);

}
