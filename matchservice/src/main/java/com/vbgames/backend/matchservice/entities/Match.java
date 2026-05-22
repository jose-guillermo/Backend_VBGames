package com.vbgames.backend.matchservice.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.vbgames.backend.matchservice.enums.FinishReason;
import com.vbgames.backend.matchservice.enums.MatchStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "player_1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player_2_id")
    private User player2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "played_at")
    private long playedAt;

    private MatchStatus status;
    private FinishReason finishReason;

    private boolean asynchronous;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Move> moves;

    public Match(User player1, User player2, Game game, boolean asynchronous) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;
        this.game = game;
        this.status = MatchStatus.PROPOSED;
        this.finishReason = null;
        this.asynchronous = asynchronous;
        this.playedAt = Instant.now().toEpochMilli();
    }

    // public sendMove(Move move) {

    // }
}
