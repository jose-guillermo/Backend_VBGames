package com.vbgames.backend.productservice.exceptions;

public class InsufficientCoinsException extends RuntimeException{

    public InsufficientCoinsException() {
        super();
    }

    public InsufficientCoinsException(String message) {
        super(message);
    }
}
