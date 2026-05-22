package com.vbgames.backend.matchservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.matchservice.dtos.MatchResponse;
import com.vbgames.backend.matchservice.dtos.MoveRequest;
import com.vbgames.backend.matchservice.services.MatchQueueService;
import com.vbgames.backend.matchservice.services.MatchService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchQueueService matchQueueService;

    @Operation(
        summary = "Buscar partida",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @PostMapping("/games/{gameIdString}/matchmaking")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void searchMatch(
        @PathVariable @IsUUID String gameIdString,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        matchQueueService.searchMatch(userId, UUID.fromString(gameIdString));
    }

    @Operation(
        summary = "Cancelar busqueda de partida",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @PostMapping("/games/{gameIdString}/matchmaking/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSearchMatch(
        @PathVariable @IsUUID String gameIdString,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        matchQueueService.cancelSearchMatch(userId, UUID.fromString(gameIdString));
    }
    
    @Operation(
        summary = "Obtener partida por id",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @GetMapping("/{matchIdString}")
    @ResponseStatus(HttpStatus.OK)
    public MatchResponse getMatch(
        @PathVariable @IsUUID String matchIdString,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        return matchService.getMatch(UUID.fromString(matchIdString));
    }

    @Operation(
        summary = "Enviar movimiento",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @PostMapping("/{matchIdString}/move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMove(
        @RequestHeader("X-User-Id") UUID userId,
        @Valid @RequestBody MoveRequest moveRequest,
        BindingResult result
    ) {
        System.out.println("hola" + moveRequest);
        validation(result);
        matchService.sendMove(moveRequest, userId);
        System.out.println("Enviando movimiento: " + moveRequest);
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
