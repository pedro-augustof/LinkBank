package com.fourcamp.linkbank.exceptions;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(){
        super("Cliente não encontrado");
    }
}
