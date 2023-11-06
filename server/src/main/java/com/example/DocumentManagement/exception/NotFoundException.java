package com.example.DocumentManagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private final String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
