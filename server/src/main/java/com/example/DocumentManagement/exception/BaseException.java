package com.example.DocumentManagement.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseException {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private Object message;

    public BaseException(HttpStatus status, LocalDateTime timestamp, Object message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }
}
