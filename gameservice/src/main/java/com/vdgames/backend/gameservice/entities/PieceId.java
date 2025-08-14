package com.vdgames.backend.gameservice.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PieceId implements Serializable{
    
    private String name;

    private String color;

    private String gameId;
}
