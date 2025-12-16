package com.vbgames.backend.authservice.exceptions;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException() {
        super("Credenciales inválidos");
    }

}
