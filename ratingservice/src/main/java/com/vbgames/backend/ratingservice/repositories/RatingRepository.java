package com.vbgames.backend.ratingservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.ratingservice.entities.Rating;
import com.vbgames.backend.ratingservice.entities.RatingId;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    List<Rating> findTop50ByGameIdOrderByRatingDesc(UUID gameId);

    List<Rating> findAllByUserId(UUID userId);

    Optional<Rating> findAllByUserIdAndGameId(UUID userId, UUID gameId);


}
