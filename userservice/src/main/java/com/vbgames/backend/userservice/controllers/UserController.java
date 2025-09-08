package com.vbgames.backend.userservice.controllers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.userservice.entities.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController("/user-service")
public class UserController {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable @IsUUID @NotBlank String id) {
        return new User();
    }

    @PatchMapping("/{idUser}/favourite-game/{idGame}")
    @ResponseStatus(HttpStatus.OK)
    public User updateFavouriteGame(@Valid @RequestBody @IsUUID @NotBlank UUID idGame, @PathVariable @IsUUID @NotBlank UUID idUser) {
        return new User();
    }
    
    private void validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        throw new RequestValidationException(errors);
    }
}
