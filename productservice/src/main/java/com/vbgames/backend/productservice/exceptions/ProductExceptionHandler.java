package com.vbgames.backend.productservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ProductExceptionHandler extends GlobalExceptionHandler{

    @ExceptionHandler(InsufficientCoinsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerInsufficientCoinsException(InsufficientCoinsException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request, ErrorCode.INSUFFICIENT_COINS);
    }

    @ExceptionHandler(ProductAlreadyOwnedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerProductAlreadyOwnedException(ProductAlreadyOwnedException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request, ErrorCode.PRODUCT_ALREADY_OWNED);
    }
}
