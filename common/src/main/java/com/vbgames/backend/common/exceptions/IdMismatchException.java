package com.vbgames.backend.common.exceptions;

public class IdMismatchException extends RuntimeException{

    public IdMismatchException() {
        super();
    }

    public IdMismatchException(String message) {
        super(message);
    }
}
