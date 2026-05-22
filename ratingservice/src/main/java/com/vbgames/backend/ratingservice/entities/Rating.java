package com.vbgames.backend.ratingservice.entities;

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
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    @EmbeddedId
    private RatingId id;

    private short rating;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @MapsId("gameId")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    public Rating(User user, Game game, short rating) {
        this.user = user;
        this.game = game;
        this.id = new RatingId(user.getId(), game.getId());
        this.rating = rating;
    }

}
