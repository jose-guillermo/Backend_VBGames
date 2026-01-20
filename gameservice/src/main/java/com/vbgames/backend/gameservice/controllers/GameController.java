package com.vbgames.backend.gameservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.gameservice.dtos.GameCreateRequest;
import com.vbgames.backend.gameservice.dtos.GameResponse;
import com.vbgames.backend.gameservice.dtos.GameUpdateRequest;
import com.vbgames.backend.gameservice.services.GameService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping ("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @Operation(
        summary = "Crear juego",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 409 → GAME_ALREADY_EXISTS"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse create(@Valid @RequestBody GameCreateRequest gameDto, BindingResult result) {
        validation(result);
        return gameService.create(gameDto);
    }

    @Operation(
        summary = "Conseguir todos los juegos"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameResponse> getGames() {
        return Collections.unmodifiableList(gameService.getAll());
    }

    @Operation(
        summary = "Actualizar juego por id",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND\n" +
            "- 409 → GAME_ALREADY_EXISTS"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse update(@Valid @RequestBody GameUpdateRequest gameDto, BindingResult result, @Valid @PathVariable @IsUUID String id) {
        validation(result);

        UUID gameId = UUID.fromString(id);

        return gameService.update(gameDto, gameId);
    }

    private void validation(BindingResult result) {
        if (!result.hasFieldErrors()) return;

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        throw new RequestValidationException(errors);
    }
}