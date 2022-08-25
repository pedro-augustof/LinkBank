package com.fourcamp.linkbank.exceptions;

public class InsufficientLimitException extends RuntimeException{

    public InsufficientLimitException(String message) {
        super(message);
    }
}
