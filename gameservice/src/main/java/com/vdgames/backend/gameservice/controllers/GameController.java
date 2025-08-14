package com.vdgames.backend.gameservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vdgames.backend.gameservice.dto.GameDto;
import com.vdgames.backend.gameservice.services.GameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping ("/gameService")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/createGame")
    public ResponseEntity<?> createGame(@Valid @RequestBody GameDto gameDto, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.ok(gameService.createGame(gameDto));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
 