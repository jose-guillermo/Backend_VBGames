package com.vbgames.backend.matchservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.events.MatchFoundEvent;
import com.vbgames.backend.common.events.MatchPlayerDisconnectedEvent;
import com.vbgames.backend.common.events.UserDisconnectedEvent;
import com.vbgames.backend.matchservice.entities.Game;
import com.vbgames.backend.matchservice.entities.Match;
import com.vbgames.backend.matchservice.entities.MatchQueue;
import com.vbgames.backend.matchservice.entities.Rating;
import com.vbgames.backend.matchservice.entities.User;
import com.vbgames.backend.matchservice.mappers.MatchMapper;
import com.vbgames.backend.matchservice.repositories.MatchQueueRepository;
import com.vbgames.backend.matchservice.repositories.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchQueueService {

    private final MatchRepository matchRepository;
    private final MatchQueueRepository matchQueueRepository;
    private final UserService userService;
    private final GameService gameService;
    private final RatingService ratingService;
    private final MatchMapper matchMapper;
    private final KafkaTemplate<String, MatchFoundEvent> kafkaTemplate;

    @Transactional
    public void searchMatch(UUID userId, UUID gameId) {
        User user = userService.getUserById(userId);
        Game game = gameService.getGameById(gameId);
        Rating rating = ratingService.getRating(user, game);

        MatchQueue matchQueue = matchQueueRepository.findMatchAndDelete(gameId, rating.getRating());

        if (matchQueue == null) {
            MatchQueue newMatchQueue = new MatchQueue(user, game, rating.getRating());
            matchQueueRepository.save(newMatchQueue);
            return;
        }

        Match newMatch = new Match(matchQueue.getUser(), user, game, false);
        newMatch = matchRepository.save(newMatch);

        sendMatchFoundEvent(newMatch);
    }

    @Transactional
    public void cancelSearchMatch(UUID userId, UUID gameId) {
        matchQueueRepository.deleteAllByUserIdAndGameId(userId, gameId);
    }

    public void sendMatchFoundEvent(Match match) {
        MatchFoundEvent event = matchMapper.toMatchFoundEvent(match);
        kafkaTemplate.send("match.found", event);
    }

    @KafkaListener(topics = "match.player.disconnected")
    @Transactional
    public void handleMatchDisconectedEvent(MatchPlayerDisconnectedEvent event) {
        Match match = matchRepository.findMatchAndDelete(event.getMatchId());
        if (match == null) return;

        Game game = gameService.getReferenceById(match.getGame().getId());
        for (UUID playerId : event.getPlayerIds()) {
            if (!event.getUnavailablePlayerIds().contains(playerId)) {
                User user = userService.getReferenceById(playerId);
                Rating rating = ratingService.getRating(user, game);
                MatchQueue newMatchQueue = new MatchQueue(user, game, rating.getRating());
                matchQueueRepository.save(newMatchQueue);
            }
        }
    }

    @KafkaListener(topics = "user.disconnected")
    @Transactional
    public void handleUserDisconnectedEvent(UserDisconnectedEvent event) {
        matchQueueRepository.deleteAllByUserId(event.getId());
    }

}
