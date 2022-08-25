package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.dto.Login;
import com.fourcamp.linkbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    AccountService accountService;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Account login(@RequestBody Login login) {
        if (login.getCpf() == null || login.getPassword() == null) {
            throw new InvalidInputException("CPF e senha faltando");
        }

        return accountService.login(login);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Account resetPassword(@RequestBody Login login) {
        if (login.getCpf() == null || login.getPassword() == null) {
            throw new InvalidInputException("CPF e senha faltando");
        }

        return accountService.loginresetpassword(login);
    }
}
