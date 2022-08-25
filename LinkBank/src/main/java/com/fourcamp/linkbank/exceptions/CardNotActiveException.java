package com.fourcamp.linkbank.exceptions;

public class CardNotActiveException extends RuntimeException{
    public CardNotActiveException(String message) {
        super(message);
    }
}
