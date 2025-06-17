package com.jcgfdev.flycommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    private static final String DESCRIPTION = "Dato ya existe";

    public AlreadyExistsException(String message) {
        super(String.format("%s. %s", DESCRIPTION, message));
    }
}
