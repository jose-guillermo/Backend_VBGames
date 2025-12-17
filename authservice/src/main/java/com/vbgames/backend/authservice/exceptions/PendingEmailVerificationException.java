package com.vbgames.backend.authservice.exceptions;

public class PendingEmailVerificationException extends RuntimeException{
    

    public PendingEmailVerificationException(String message) {
        super(message);
    }

}
