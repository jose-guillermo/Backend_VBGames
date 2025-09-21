package com.vbgames.backend.common.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public abstract class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class, 
        NoResourceFoundException.class, 
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRuntimeException(Exception ex, HttpServletRequest request) {
        String message = "Ruta no encontrada: " + ex.getMessage();
        return new ApiError(message, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        return  new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(IdMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerIdMismatchException(IdMismatchException ex, HttpServletRequest request) {
        return new ApiError(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerRequestValidationException(RequestValidationException ex, HttpServletRequest request) {
        return new ApiError("Errores de validación", ex.getErrors(), HttpStatus.BAD_REQUEST, request);
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
        
        return new ApiError("Errores de validación", fieldErrors, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return new ApiError("El cuerpo de la petición es obligatorio y no puede estar vacío.", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleDatabaseException(DataAccessException ex, HttpServletRequest request) {
        String message = "Error en la base de datos: " + ex.getMessage();
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handlerBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return new ApiError("Error de autenticación", HttpStatus.UNAUTHORIZED, request);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex, HttpServletRequest request) {
        String message = "Error inesperado: " + ex.getMessage();
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
