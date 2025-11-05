package com.vbgames.backend.productservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;
import com.vbgames.backend.productservice.exceptions.InsufficientCoinsException;
import com.vbgames.backend.productservice.exceptions.ProductAlreadyOwnedException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandlerExceptionController extends GlobalExceptionHandler{

    @ExceptionHandler(InsufficientCoinsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerInsufficientCoinsException(InsufficientCoinsException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ProductAlreadyOwnedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerProductAlreadyOwnedException(ProductAlreadyOwnedException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request);
    }
}
