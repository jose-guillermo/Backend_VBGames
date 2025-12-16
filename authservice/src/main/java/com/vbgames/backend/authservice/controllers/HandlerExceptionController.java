package com.vbgames.backend.authservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandlerExceptionController extends GlobalExceptionHandler {
    
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerMessagingException(MessagingException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerResourceNotFoundException(IllegalStateException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerResourceNotFoundException(ExpiredJwtException ex, HttpServletRequest request) {
        return new ApiError("Token expirado", HttpStatus.CONFLICT, request);
    }
}
