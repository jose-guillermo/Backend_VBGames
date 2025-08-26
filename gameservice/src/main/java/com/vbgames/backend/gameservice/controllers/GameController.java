package com.vbgames.backend.gameservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.IdMismatchException;
import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.gameservice.dtos.GameDto;
import com.vbgames.backend.gameservice.services.GameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping ("/gameService")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody GameDto gameDto, BindingResult result) {
        if (result.hasErrors())
            validation(result);
        
        return ResponseEntity.ok(gameService.create(gameDto));
    }

    @GetMapping
    public ResponseEntity<?> getGames() {
        return ResponseEntity.ok(gameService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody GameDto gameDto, BindingResult result, @PathVariable @IsUUID String id) {
        if (result.hasErrors())
            validation(result);

        if(gameDto.getId() != null && !gameDto.getId().equals(id))
            throw new IdMismatchException("El id del juego de la url y del body no coinciden");

        UUID idGame = UUID.fromString(id);

        return ResponseEntity.ok(gameService.update(gameDto, idGame));
    }
    
    private void validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        throw new RequestValidationException(errors);
    }
}
 