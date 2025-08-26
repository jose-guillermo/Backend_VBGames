package com.vbgames.backend.gameservice.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceId implements Serializable{
    
    private String name;

    private String color;

    private UUID gameId;
}
