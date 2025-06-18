package com.jcgfdev.flycommerce.exception;

public class SecurityException extends RuntimeException {
    private static final String DESCRIPTION = "security exception";

    public SecurityException(String message) {
        super(String.format("%s. %s", DESCRIPTION, message));
    }
}
