package com.vbgames.backend.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.common.exceptions.GlobalExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handlerExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        return new ApiError("Token expirado", HttpStatus.UNAUTHORIZED, request, ErrorCode.TOKEN_EXPIRED);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handlerInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        return new ApiError("Credenciales inválidas", HttpStatus.UNAUTHORIZED, request, ErrorCode.INVALID_CREDENTIALS);
    }
    
    @ExceptionHandler(PendingEmailVerificationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerIllegalStateException(PendingEmailVerificationException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request, ErrorCode.PENDING_EMAIL_VERIFICATION);
    }

    @ExceptionHandler(MailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerMessagingException(MailSendingException ex, HttpServletRequest request) {
        log.error("Mail sending failed", ex);
        return new ApiError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, ErrorCode.MAIL_SENDING_ERROR);
    }
}
