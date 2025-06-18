package com.jcgfdev.flycommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {
    private static final String DESCRIPTION = "Dato no encontrado";

    public DataNotFoundException(String message) {
        super(String.format("%s. %s", DESCRIPTION, message));
    }
}
