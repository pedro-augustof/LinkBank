package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody Client client) {
        if (client.getPassword() == null || client.getIncome() == null || client.getCpf() == null || client.getRg() == null || client.getFullname() == null || client.getBirthday() == null) {
            throw new InvalidInputException("Dados invalidos");
        }

        return registerService.register(client);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String update(@RequestBody Client client) {
        if (client.getPassword() == null || client.getIncome() == null || client.getCpf() == null || client.getAddress().getCep() == null || client.getRg() == null || client.getFullname() == null || client.getBirthday() == null) {
            throw new InvalidInputException("Dados incompletos");
        }

        return registerService.update(client);
    }
}