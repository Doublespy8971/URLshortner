package com.pm.urlshortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCustomCodeException extends RuntimeException {
    public InvalidCustomCodeException(String message) {
        super(message);
    }
}

