package com.example.globalrequest.config;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/29
 */
@Data
public class ApiError {
    private Integer status;

    private String message;

    private Exception exception;

    public ApiError(HttpStatus internalServerError, String message, Exception ex) {
        this.status = internalServerError.value();
        this.message = message;
        this.exception = ex;
    }

    public ApiError(HttpStatus internalServerError, String message) {
        this.status = internalServerError.value();
        this.message = message;
    }
}
