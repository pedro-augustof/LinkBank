package com.fourcamp.linkbank.exceptions;

public class CardNotFoundException  extends RuntimeException{

    public CardNotFoundException(String message) {
        super(message);
    }
}
