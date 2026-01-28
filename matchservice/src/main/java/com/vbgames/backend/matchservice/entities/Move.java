package com.vbgames.backend.matchservice.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Move {

    @EmbeddedId
    private MoveId id;

    private short fromRow;
    private short fromCol;

    private short toRow;
    private short toCol;

    private boolean gameOver;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @MapsId("matchId")
    private Match match;
}
