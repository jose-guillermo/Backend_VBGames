package com.vdgames.backend.gameservice.entities;

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
@Table(name = "pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Piece {

    @EmbeddedId
    private PieceId id;

    @MapsId("gameId") // debe coincidir con el nombre del campo en PieceId
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}
