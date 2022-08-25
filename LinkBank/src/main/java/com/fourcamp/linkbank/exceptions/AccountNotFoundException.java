package com.fourcamp.linkbank.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(){
        super("Conta não encontrada");
    }

}
