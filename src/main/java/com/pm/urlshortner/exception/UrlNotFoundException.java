package com.pm.urlshortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException {
    public static final String MESSAGE = "URL not found or has expired";
    public UrlNotFoundException() {
        super(MESSAGE);
    }
}

