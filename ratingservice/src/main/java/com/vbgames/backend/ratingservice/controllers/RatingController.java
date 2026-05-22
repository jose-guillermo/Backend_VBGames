package com.vbgames.backend.ratingservice.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.ratingservice.dtos.GameRatingResponse;
import com.vbgames.backend.ratingservice.dtos.UserRatingResponse;
import com.vbgames.backend.ratingservice.services.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @Operation(
        summary = "Devuelve el top 50 del ranking de un juego",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND"
    )
    @GetMapping("/{gameid}/ranking")
    @ResponseStatus(HttpStatus.OK)
    public List<GameRatingResponse> getRanking(@PathVariable @IsUUID String gameid) {
        return ratingService.getAllRatingsByGameDescended(UUID.fromString(gameid));
    }

    @Operation(
        summary = "Devuelve todos tus ratings",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRatingResponse> getMyRatings(@RequestHeader("X-User-Id") UUID userId) {
        return ratingService.getAllRatingsByUser(userId);
    }

    @Operation(
        summary = "Devuelve todos los ratings de un usuario",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRatingResponse> getRatingsUser(@PathVariable @IsUUID String userId) {
        return ratingService.getAllRatingsByUser(UUID.fromString(userId));
    }
}
