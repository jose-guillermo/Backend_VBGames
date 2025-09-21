package com.vbgames.backend.userservice.controllers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.userservice.dtos.LoginRequest;
import com.vbgames.backend.userservice.dtos.LoginResponse;
import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.services.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable @IsUUID @NotBlank String id) {
        return userService.getUser(UUID.fromString(id));
    }

    @PatchMapping("/{idUser}/favourite-game/{idGame}")
    @ResponseStatus(HttpStatus.OK)
    public User updateFavouriteGame(@Valid @RequestBody @IsUUID @NotBlank UUID idGame, @PathVariable @IsUUID @NotBlank UUID idUser) {
        return new User();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserResponse registerUser(@Valid @RequestBody UserRequest request, BindingResult result) {
        if(result.hasFieldErrors())
            validation(result); 

        return userService.registerUser(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if(result.hasFieldErrors())
            validation(result); 
        
        return userService.validateCredentials(request);
    }
    
    private void validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        throw new RequestValidationException(errors);
    }
}