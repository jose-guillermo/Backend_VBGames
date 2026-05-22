package com.vbgames.backend.ratingservice.entities;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingId implements Serializable {

    private UUID userId;
    private UUID gameId;

}
