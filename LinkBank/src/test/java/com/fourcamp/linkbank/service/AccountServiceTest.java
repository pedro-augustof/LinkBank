package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.dto.Login;
import com.fourcamp.linkbank.exceptions.AccountNotFoundException;
import com.fourcamp.linkbank.exceptions.ClientNotFoundException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.*;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.ClientRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @Autowired
    Client client;

    @Autowired
    Pix pix;

    @Autowired
    Card card;

    @Mock
    ClientRepository clientRepository;
    LocalDate localDate = LocalDate.now();

    @InjectMocks
    private AccountService service;

    @Mock
    AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        createAccount();
    }

    private void createAccount() {
        this.card = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 0.00, 6000.00, "PREMIUM", 20000.00, 155);
        this.pix = new Pix(1L, "18565851745", "julio@manumentirosa.com", "21985857474");
        this.client = new Client(1L, "18565851745", "226176046", "Julio Cézar Oliveira", "24/01/2002", 1000.00,"123456","julio@manumentirosa.com","padeiro",null, "21985857474" );
        this.account = new Account(1L, "12345-1", "0001",600.0, client, pix, card, localDate, 500.00, 900.00);
    }
    @Test
    void DeveRetornarExcecaoQuandoClienteNaoEncontrado(){
       when(clientRepository.findClientByCpf(eq("18565851745"))).thenReturn(null);
        ClientNotFoundException clientNotFoundException = Assert.assertThrows(ClientNotFoundException.class,()->service.login(new Login( "18565851745","123456")));
        Assertions.assertEquals("Cliente não encontrado", clientNotFoundException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoSenhaForInvalida(){
        Login login = new Login( "18565851745", "1010");
        when(clientRepository.findClientByCpf(login.getCpf())).thenReturn(client);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()->service.login(login));
        Assertions.assertEquals("Senha inválida", invalidInputException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoNaoEncontrarCliente(){
        when(clientRepository.findClientByCpf(eq("12312312312"))).thenReturn(null);
        ClientNotFoundException clientNotFoundException = Assert.assertThrows(ClientNotFoundException.class,()->service.loginresetpassword(new Login( "12312312312", "8080")));
        Assertions.assertEquals("Cliente não encontrado", clientNotFoundException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoSenhaIgual(){
        Login login = new Login( "12312312312", "123456");
        when(clientRepository.findClientByCpf(login.getCpf())).thenReturn(client);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()->service.loginresetpassword(login));
        Assertions.assertEquals("Senha igual", invalidInputException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoDepositoNaoRealizado(){
        when(accountRepository.findAccountByNumber(eq("12345-1"))).thenReturn(null);
        AccountNotFoundException accountNotFoundException = Assert.assertThrows(AccountNotFoundException.class,()-> service.deposit(account));
        Assertions.assertEquals("Conta não encontrada", accountNotFoundException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoDepositoNaoSalvar(){
        when(accountRepository.findAccountByNumber(eq("12345-1")))   .thenReturn(null);
        AccountNotFoundException accountNotFoundException = Assert.assertThrows(AccountNotFoundException.class,()-> service.depositInSavingsAccount(account));
        Assertions.assertEquals("Conta não encontrada", accountNotFoundException.getMessage());
    }
    @Test
    void DeveRetornarExcecaoQuandoPagamentoNaoRealizado(){
        //when()
        //
    }

}
