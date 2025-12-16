package com.vbgames.backend.authservice.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(
        @Valid @RequestBody @ConfirmPassword RegisterRequest request, 
        BindingResult result
    ) {
        validation(result);

        return userService.registerUser(request);
    }

    @GetMapping("/verify/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmail(@PathVariable String token) {
        userService.verifyEmail(token);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void postMethodName(
        @RequestBody @ConfirmPassword RegisterRequest request,
        BindingResult result,
        HttpServletResponse response
    ) {
        validation(result);
        userService.login(request, response);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public void postMethodName(
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