package com.vbgames.backend.matchservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.vbgames.backend.common.events.RatingUpsertedEvent;
import com.vbgames.backend.matchservice.entities.Game;
import com.vbgames.backend.matchservice.entities.Rating;
import com.vbgames.backend.matchservice.entities.User;
import com.vbgames.backend.matchservice.repositories.GameRepository;
import com.vbgames.backend.matchservice.repositories.RatingRepository;
import com.vbgames.backend.matchservice.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "rating.upserted")
    @Transactional
    private void handleRatingUpsertedEvent(RatingUpsertedEvent event) {
        Rating rating = ratingRepository.findByUserIdAndGameId(event.getUserId(), event.getGameId()).orElse(null);

        if(rating == null) {
            User user = userRepository.getReferenceById(event.getUserId());
            Game game = gameRepository.getReferenceById(event.getGameId());
            rating = new Rating(user, game, event.getRating());
        }

        ratingRepository.save(rating);
    }

    @Transactional
    public Rating getRating(User user, Game game) {
        return ratingRepository.findByUserIdAndGameId(user.getId(), game.getId())
            .orElse(new Rating(user, game, (short) 1200));
    }

}
