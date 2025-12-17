package com.vbgames.backend.common.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vbgames.backend.common.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public abstract class GlobalExceptionHandler {

    protected static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, request, ex.getErrorCode());
    }

    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class, 
        NoResourceFoundException.class, 
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRouteNotFoundException(Exception ex, HttpServletRequest request) {
        String message = "Ruta no encontrada: " + ex.getMessage();
        return new ApiError(message, HttpStatus.NOT_FOUND, request, ErrorCode.ROUTE_NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        return  new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request, ex.getErrorCode());
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerRequestValidationException(RequestValidationException ex, HttpServletRequest request) {
        return new ApiError("Errores de validación", ex.getErrors(), HttpStatus.BAD_REQUEST, request, ErrorCode.VALIDATION_ERROR);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMethodValidationException(HandlerMethodValidationException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        // Sacamos de la excepcion los campos y los mensajes de error de las validaciones
        ex.getAllErrors().forEach(err -> {
            String field = err.getCodes()[1].split("\\.")[1];
            String message = err.getDefaultMessage();

            fieldErrors.put(field, message);
        });

        return new ApiError("Errores de validación", fieldErrors, HttpStatus.BAD_REQUEST, request, ErrorCode.VALIDATION_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        Throwable cause = ex.getMostSpecificCause();

        // Enum inválido
        if (cause instanceof InvalidFormatException)
            return new ApiError(
                "Se ha enviado un valor no válido para un campo enumerado.",
                HttpStatus.BAD_REQUEST,
                request,
                ErrorCode.INVALID_ENUM_VALUE
            );

        // JSON mal formado
        if (cause instanceof JsonParseException)
            return new ApiError(
                "El cuerpo de la petición tiene un formato JSON inválido.",
                HttpStatus.BAD_REQUEST,
                request,
                ErrorCode.INVALID_JSON
            );
        
        return new ApiError(
            "No se ha podido interpretar el cuerpo de la petición.",
            HttpStatus.BAD_REQUEST,
            request,
            ErrorCode.INVALID_REQUEST_BODY
        );
    }

    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handlerForbiddenActionException(ForbiddenActionException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN, request, ErrorCode.FORBIDDEN_ACTION);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleDatabaseException(DataAccessException ex, HttpServletRequest request) {
        log.error("Database error", ex);
        String message = "Error en la base de datos" ;
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR, request, ErrorCode.DATABASE_ERROR);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado", ex);
        String message = "Error interno del servidor";
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR, request, ErrorCode.INTERNAL_ERROR);
    }
}
