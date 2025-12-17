package com.vbgames.backend.friendshipservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class FriendshipExceptionHandler  extends GlobalExceptionHandler {

    @ExceptionHandler(SelfFriendRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerSelfFriendRequestException(SelfFriendRequestException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request, ex.getErrorCode());
    }
}
