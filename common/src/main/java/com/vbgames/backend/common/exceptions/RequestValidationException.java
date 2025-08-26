package com.vbgames.backend.common.exceptions;

import java.util.Map;

import lombok.Getter;

@Getter
public class RequestValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public RequestValidationException(Map<String, String> errors) {
        super("Errores de validación");
        this.errors = errors;
    }
}
