package com.vbgames.backend.friendshipservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;
import com.vbgames.backend.friendshipservice.exceptions.DuplicateFriendshipException;
import com.vbgames.backend.friendshipservice.exceptions.SelfFriendRequestException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class HandlerExceptionController  extends GlobalExceptionHandler {
    @ExceptionHandler(DuplicateFriendshipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerDuplicateFriendshipException(DuplicateFriendshipException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(SelfFriendRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerSelfFriendRequestException(SelfFriendRequestException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request);
    }
}
