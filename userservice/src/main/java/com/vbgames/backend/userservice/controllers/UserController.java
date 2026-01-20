package com.vbgames.backend.userservice.controllers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.userservice.dtos.UpdateUsernameRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Obtener usuario por id",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → USER_NOT_FOUND"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable @IsUUID String id) {
        return userService.getUser(UUID.fromString(id));
    }

    @Operation(
        summary = "Cambiar juego favorito",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → USER_NOT_FOUND\n" +
            "- 404 → GAME_NOT_FOUND" 
    )
    @PatchMapping("/favourite-game/{gameIdString}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateFavouriteGame(
        @PathVariable @IsUUID String gameIdString,
        @RequestHeader("X-User-Id") UUID userId
    ) {
        UUID gameId = UUID.fromString(gameIdString);

        return userService.updateFavouriteGame(userId, gameId);
    }

    @Operation(
        summary = "Cambiar el nombre de usuario",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → USER_NOT_FOUND\n" +
            "- 409 → USERNAME_ALREADY_EXISTS" 
    )
    @PatchMapping("/update-username")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUsername(
        @Valid @RequestBody UpdateUsernameRequest request, 
        BindingResult result, 
        @RequestHeader("X-User-Id") UUID userId
    ) {
        validation(result);

        return userService.updateUsername(request.getUsername(), userId);
    }

    @Operation(
        summary = "Cambiar el estado del usuario",
        description = "Errores posibles:\n" +
            "- 404 → USER_NOT_FOUND\n"
    )
    @PatchMapping("/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void onlineOffline(@RequestHeader("X-User-Id") UUID userId) {
        userService.onlineOffline(userId);
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