package com.vbgames.backend.common.exceptions;

public class ForbiddenActionException extends RuntimeException {

    public ForbiddenActionException() {
        super();
    }

    public ForbiddenActionException(String message) {
        super(message);
    }


}
