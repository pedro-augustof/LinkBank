package com.fourcamp.linkbank.exceptions;

public class PolicyNotFoundException extends RuntimeException{
    public PolicyNotFoundException(String message) {
        super(message);
    }
}
