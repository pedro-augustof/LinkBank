package com.fourcamp.linkbank.exceptions;

public class NotSupportedLimitException extends RuntimeException {
    public NotSupportedLimitException(String message) {
        super(message);
    }
}
