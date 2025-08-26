package com.vbgames.backend.common.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
    private String statusDescription;
    private String requestPath;
    private String requestMethod;
    private Map<String, String> fieldErrors;

    public ApiError(String message, HttpStatus statusCode, HttpServletRequest request) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = statusCode.value();
        this.statusDescription = statusCode.getReasonPhrase();
        this.requestPath = request.getRequestURI();
        this.requestMethod = request.getMethod();
    }

    public ApiError(String message,Map<String, String> fieldErrors, HttpStatus status, HttpServletRequest request) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = status.value();
        this.statusDescription = status.getReasonPhrase();
        this.requestPath = request.getRequestURI();
        this.requestMethod = request.getMethod();
        this.fieldErrors = fieldErrors;
    }
}
