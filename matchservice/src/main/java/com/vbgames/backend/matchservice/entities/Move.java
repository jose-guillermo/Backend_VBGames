package com.vbgames.backend.matchservice.entities;

import java.util.List;

import com.vbgames.backend.common.dto.Action;
import com.vbgames.backend.matchservice.converters.ActionListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "moves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Move {

    @EmbeddedId
    private MoveId id;

    @Convert(converter = ActionListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Action> actions;

    private boolean gameOver;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @MapsId("matchId")
    private Match match;
}