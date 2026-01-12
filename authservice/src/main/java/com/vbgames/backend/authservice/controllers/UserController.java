package com.vbgames.backend.authservice.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;

import io.swagger.v3.oas.annotations.Operation;

import com.vbgames.backend.authservice.dtos.LoginRequest;
import com.vbgames.backend.authservice.dtos.RegisterRequest;
import com.vbgames.backend.authservice.dtos.UserResponse;
import com.vbgames.backend.authservice.services.JwtService;
import com.vbgames.backend.authservice.services.UserService;
import com.vbgames.backend.authservice.validators.ConfirmPassword;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @Operation(
        summary = "Verificar correo",
        description = "Errores posibles:\n" +
            "- 401 → TOKEN_EXPIRED"
    )
    @GetMapping("/verify/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmail(@PathVariable String token) {
        userService.verifyEmail(token);
    }

    @Operation(
        summary = "Registro de usuario",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 401 → INVALID_CREDENTIALS\n" +
            "- 409 → EMAIL_ALREADY_EXISTS\n" +
            "- 409 → PENDING_EMAIL_VERIFICATION\n"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(
        @Valid @RequestBody @ConfirmPassword RegisterRequest request, 
        BindingResult result
    ) {
        validation(result);

        return userService.registerUser(request);
    }

    @Operation(
        summary = "Login de usuario",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 401 → INVALID_CREDENTIALS\n" +
            "- 401 → TOKEN_EXPIRED"
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
        @RequestBody LoginRequest request,
        BindingResult result,
        HttpServletResponse response
    ) {
        validation(result);
        userService.login(request, response);
    }

    @Operation(
        summary = "Renovar token",
        description = "Errores posibles:\n" +
            "- 401 → INVALID_CREDENTIALS\n" +
            "- 401 → TOKEN_EXPIRED\n"
    )
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public void refresh(
        @CookieValue(name = JwtService.COOKIE_REFRESH_TOKEN) String refreshToken,
        HttpServletResponse response
    ) {
        userService.refresh(refreshToken, response);
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