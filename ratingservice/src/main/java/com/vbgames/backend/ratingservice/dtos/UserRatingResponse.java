package com.vbgames.backend.ratingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingResponse {

    private GameResponse game;
    private short rating;

}
