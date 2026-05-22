package com.vbgames.backend.matchservice.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "match_queue")
@Getter
@Setter
@NoArgsConstructor
public class MatchQueue {

    @Id
    @EmbeddedId
    private MatchQueueId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @MapsId("gameId")
    private Game game;

    private short rating;

    @Column(name = "created_at")
    private long createdAt;

     public MatchQueue(User user, Game game, short rating) {
        this.user = user;
        this.game = game;
        this.id = new MatchQueueId(user.getId(), game.getId());
        this.rating = rating;
        this.createdAt = Instant.now().toEpochMilli();
    }
}
