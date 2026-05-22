package com.vbgames.backend.ratingservice.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.MatchFinishedEvent;
import com.vbgames.backend.common.events.RatingUpsertedEvent;
import com.vbgames.backend.ratingservice.dtos.GameRatingResponse;
import com.vbgames.backend.ratingservice.dtos.UserRatingResponse;
import com.vbgames.backend.ratingservice.entities.Game;
import com.vbgames.backend.ratingservice.entities.Rating;
import com.vbgames.backend.ratingservice.entities.User;
import com.vbgames.backend.ratingservice.mappers.RatingMapper;
import com.vbgames.backend.ratingservice.repositories.RatingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final UserService userService;
    private final GameService gameService;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final KafkaTemplate<String, RatingUpsertedEvent> kafkaTemplate;
    private final int K = 32;

    public List<GameRatingResponse> getAllRatingsByGameDescended(UUID gameId) {
        gameService.exists(gameId);

        List<Rating> ranking =  ratingRepository.findTop50ByGameIdOrderByRatingDesc(gameId);
        return ratingMapper.toGameRatingResponseList(ranking);
    }

    public List<UserRatingResponse> getAllRatingsByUser(UUID userId) {
        userService.exists(userId);

        List<Rating> ratings = ratingRepository.findAllByUserId(userId);

        User user = userService.get(userId);
        List<Game> games = gameService.getAll();

        // Evito una situación de Orden(n^2)
        Set<UUID> ratedGameIds = ratings.stream().map(r -> r.getGame().getId()).collect(Collectors.toSet());

        for (Game game : games){
            if (!ratedGameIds.contains(game.getId())) {
                ratings.add(new Rating(user, game, (short) 1000));
            }
        }

        return ratingMapper.toUserRatingResponseList(ratings);
    }

    @KafkaListener(topics = "match.finished")
    @Transactional
    public void handleMatchFinishedEvent(MatchFinishedEvent event) {
        User user1 = userService.get(event.getPlayer1Id());
        User user2 = userService.get(event.getPlayer2Id());
        Game game = gameService.get(event.getGameId());

        Rating rating1 = ratingRepository
            .findAllByUserIdAndGameId(event.getPlayer1Id(), event.getGameId())
            .orElseGet(() -> new Rating(user1, game, (short) 1000));
        Rating rating2 = ratingRepository
            .findAllByUserIdAndGameId(event.getPlayer2Id(), event.getGameId())
            .orElseGet(() -> new Rating(user2, game, (short) 1000));

        // Formula de https://en.wikipedia.org/wiki/Elo_rating
        double expectedScoreP1 = 1/(1 + Math.pow(10, (rating2.getRating() - rating1.getRating()) / 400.0));
        double expectedScoreP2 = 1 - expectedScoreP1;

        double scoreP1;
        double scoreP2;

        if(event.getWinnerId() == null) {
            scoreP1 = 0.5;
            scoreP2 = 0.5;
        } else if(event.getWinnerId().equals(event.getPlayer1Id())) {
            scoreP1 = 1;
            scoreP2 = 0;
        } else {
            scoreP1 = 0;
            scoreP2 = 1;
        }
        
        short newRatingP1 = (short) Math.round(
            rating1.getRating() + K * (scoreP1 - expectedScoreP1)
        );

        short newRatingP2 = (short) Math.round(
            rating2.getRating() + K * (scoreP2 - expectedScoreP2)
        );
        
        rating1.setRating(newRatingP1);
        rating2.setRating(newRatingP2);
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        sendRatingUpsertedEvent(rating1);
        sendRatingUpsertedEvent(rating2);
    }

    @Transactional
    private void sendRatingUpsertedEvent(Rating rating) {
        RatingUpsertedEvent ratingUpsertedEvent = new RatingUpsertedEvent(rating.getUser().getId(), rating.getGame().getId(), rating.getRating());
        kafkaTemplate.send("rating.upserted", ratingUpsertedEvent);
    }
}
