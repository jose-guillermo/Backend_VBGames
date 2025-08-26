package com.vbgames.backend.gameservice.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "game")
public class Piece {

    @EmbeddedId
    private PieceId id;

    @ManyToOne
    // @JoinColumn(name = "game_id", insertable = false, updatable = false)
    @JoinColumn(name = "game_id")
    @MapsId("gameId")
    private Game game;
}
