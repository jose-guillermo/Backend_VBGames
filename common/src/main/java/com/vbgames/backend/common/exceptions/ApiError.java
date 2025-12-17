package com.vbgames.backend.common.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vbgames.backend.common.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private ErrorCode errorCode;

    public ApiError(
            String message, 
            HttpStatus statusCode, 
            HttpServletRequest request, 
            ErrorCode errorCode
    ) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = statusCode.value();
        this.statusDescription = statusCode.getReasonPhrase();
        this.requestPath = request.getRequestURI();
        this.requestMethod = request.getMethod();
        this.errorCode = errorCode;
    }

    public ApiError(
            String message, 
            Map<String, String> fieldErrors, 
            HttpStatus status, 
            HttpServletRequest request,
            ErrorCode errorCode
    ) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = status.value();
        this.statusDescription = status.getReasonPhrase();
        this.requestPath = request.getRequestURI();
        this.requestMethod = request.getMethod();
        this.fieldErrors = fieldErrors;
        this.errorCode = errorCode;
    }
}
