package com.vbgames.backend.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandlerExceptionController extends GlobalExceptionHandler {
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handlerBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return new ApiError("Error de autenticación", HttpStatus.UNAUTHORIZED, request);
    }
}
