package com.vbgames.backend.common.exceptions;

import com.vbgames.backend.common.enums.ErrorCode;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public ResourceNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
