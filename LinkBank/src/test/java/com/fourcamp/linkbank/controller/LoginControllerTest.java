package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.dto.Login;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AccountService accountService;

    private Login login;

    private Account account;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        startLogin();
    }

    private void startLogin() {
        this.login = new Login("561589915", "ioasdnurv");
        this.account = new Account(5L, null, null, null, null, null, null, null, null, null);
    }

    @Test
    void DeveRetornarContaNoLogin(){

        when(accountService.login(login)).thenReturn(account);

        Account account1 = loginController.login(login);

        Assertions.assertEquals(account.getId(), account1.getId());
    }

    @Test
    void DeveDarInvalidInputExceptionNoLogin(){
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> loginController.login(new Login()));

        Assertions.assertEquals("CPF e senha faltando", exception.getMessage());
    }

    @Test
    void DeveRetornarContaNoResetDeSenha(){
        when(loginController.resetPassword(login)).thenReturn(account);

        Account accountResponse = loginController.resetPassword(login);

        Assertions.assertSame(account, accountResponse);
    }

    @Test
    void DeveDarInvalidInputExceptionNoResetDeSenha(){
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> loginController.resetPassword(new Login()));

        Assertions.assertEquals("CPF e senha faltando", exception.getMessage());
    }
}
