package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.*;
import com.fourcamp.linkbank.service.AccountService;
import com.fourcamp.linkbank.service.ExtractService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

    @InjectMocks
    AccountController accountController;

    @Mock
    AccountService accountService;

    @Mock
    Account account;

    @Mock
    ExtractService extractService;

    @Mock
    TransactionPix transactionPix;


    @Mock
    TransactionTransfer transactionTransfer;

    LocalDate date = LocalDate.now();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        createAccount();
        createTransactionPix();
    }

    private void createTransactionPix() {
        this.transactionPix = new TransactionPix(200.00,"21/10/2022",1L,"lucas@gmail.com");
    }

    private void createAccount() {
        this.account = new Account(1L, "4326-5", "0001", 700.00, new Client(1L, "08524461446", "111772473", "Lucas Egito", "07/09/2003", 300.00, "123456", "lucas@gmail.com", "Padeiro", new Address(), "81979019154"), new Pix(1L, "08534461446", "lucas@hotmail.com", "81979019154"), new Card(), date, 600.00, 700.00);
    }

    @Test
    void RetornoBalance() {
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.getBalance(account));
        Assertions.assertEquals("Id da conta não existe", invalidInputException.getMessage());
    }

    @Test
    void RetornarOresponseEntityNoGetBalance() {
        when(accountService.getBalance(account.getId())).thenReturn(account.getBalance());
        Assertions.assertEquals(new ResponseEntity(accountService.getBalance(account.getId()), HttpStatus.OK), accountController.getBalance(account));

    }

    @Test
    void RetornaOsavingsBalance() {
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.getSavingsAccountBalance(account));
        Assertions.assertEquals("Id Invalido", invalidInputException.getMessage());
    }

    @Test
    void RetornarOresponseEntityNoGetSavingsAccountBalance() {
        when(accountService.getSavingsAccountBalance(account.getId())).thenReturn(account.getSavingsAccountBalance());
        Assertions.assertEquals(new ResponseEntity<>(accountService.getSavingsAccountBalance(account.getId()), HttpStatus.OK), accountController.getSavingsAccountBalance(account));
    }

    @Test
    void retornoDeposito() {
        account.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeDeposit(account));
        Assertions.assertEquals("Não foi possivel realizar o deposito na conta", invalidInputException.getMessage());
    }

    @Test
    void retornoDepositoO() {
        account.setBalance(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeDeposit(account));
        Assertions.assertEquals("Não foi possivel realizar o deposito na conta", invalidInputException.getMessage());
    }

    @Test
    void retornarOresponseEntityNoDeposit() {
        Assertions.assertEquals(new ResponseEntity<>(accountService.deposit(account), HttpStatus.OK), accountController.makeDeposit(account));
    }

    @Test
    void retornoDepositSavingsAccount() {
        account.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeSavingsAccountDeposit(account));
        Assertions.assertEquals("Não foi possivel realizar o deposito na conta", invalidInputException.getMessage());
    }

    @Test
    void retornoDepositSavingsAccountt() {
        account.setSavingsAccountBalance(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeSavingsAccountDeposit(account));
        Assertions.assertEquals("Não foi possivel realizar o deposito na conta", invalidInputException.getMessage());
    }

    @Test
    void retornarOresponseEntityNoDepositSavings() {
        Assertions.assertEquals(new ResponseEntity<>(accountService.depositInSavingsAccount(account), HttpStatus.OK), accountController.makeSavingsAccountDeposit(account));
    }

    @Test
    void retornoExceptionTransferId() {
        transactionTransfer.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeTransfer(transactionTransfer));
        Assertions.assertEquals("Não foi possivel realizar a transferencia", invalidInputException.getMessage());

    }

    @Test
    void retornoExceptionTransferValue() {
        transactionTransfer.setValue(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeTransfer(transactionTransfer));
        Assertions.assertEquals("Não foi possivel realizar a transferencia", invalidInputException.getMessage());
    }

    @Test
    void retornoExceptionTransferNumber() {
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeTransfer(transactionTransfer));
        Assertions.assertEquals("Não foi possivel realizar a transferencia", invalidInputException.getMessage());
    }

    @Test
    void RetornoExcepitionTransferAgency() {
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeTransfer(transactionTransfer));
        Assertions.assertEquals("Não foi possivel realizar a transferencia", invalidInputException.getMessage());
    }

    @Test
    void RetornoExcepitionTransferFullName() {
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.makeTransfer(transactionTransfer));
        Assertions.assertEquals("Não foi possivel realizar a transferencia", invalidInputException.getMessage());
    }

    @Test
    void RetornarOresponseEntityNaTransferencia() {
        TransactionTransfer transactionTransfer1 = new TransactionTransfer(200.00, "12/10/2024", 1L, account);
        Assertions.assertEquals(new ResponseEntity<>(accountService.transfer(transactionTransfer1), HttpStatus.OK), accountController.makeTransfer(transactionTransfer1));
    }

    @Test
    void RetornoPayBill() {
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.payBill(account));
        Assertions.assertEquals("ID da conta nulo ou zero", invalidInputException.getMessage());

    }

    @Test
    void RetornarOresponseEntityNoPayBill(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.payBill(account),HttpStatus.OK),accountController.payBill(account));
    }
    @Test
    void RetornarExtract(){
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> accountController.getExtract(account));
        Assertions.assertEquals("Id da conta é nulo", invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoExtract(){
        Assertions.assertEquals(new ResponseEntity<>(extractService.listById(account.getId()), HttpStatus.OK), accountController.getExtract(account));
    }
    @Test
    void retornarPixEmail(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.pixEmail(transactionPix));
        Assertions.assertEquals("ID da transação Pix nulo ou zero", invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoPixEmail(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.pixEmail(transactionPix), HttpStatus.OK), accountController.pixEmail(transactionPix));
    }
    @Test
    void retornarTransferPixEmail(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.transferPixEmail(transactionPix));
        Assertions.assertEquals("ID da transação Pix nulo ou zero", invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoTranferPixEmail(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.transferPixEmail(transactionPix),HttpStatus.OK), accountController.transferPixEmail(transactionPix));
    }
    @Test
    void retornarOdeletePixEmail(){
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()->accountController.deletepixEmail(account));
        Assertions.assertEquals("ID da conta responsável pela chave Pix nulo ou zero",invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoDeletePixEmail(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.deletePixEmail(account), HttpStatus.OK), accountController.deletepixEmail(account));
    }
    @Test
    void retornarPixCpf(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()->accountController.pixCpf(transactionPix));
    }
    @Test
    void RetornarOresponseEntityNoPixCpf(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.pixCpf(transactionPix),HttpStatus.OK),accountController.pixCpf(transactionPix));
    }
    @Test
    void retornarTransferPixCpf(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.transferPixCpf(transactionPix));
        Assertions.assertEquals("ID da transação Pix nulo ou zero", invalidInputException.getMessage());
    }
    @Test
    void  RetornarOresponseEntityNoTranferPixCpf(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.transferPixCpf(transactionPix),HttpStatus.OK),accountController.transferPixCpf(transactionPix));
    }
    @Test
    void retornarOdeletePixCpf(){
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()->accountController.deletePixCpf(account));
        Assertions.assertEquals("ID da conta responsável pela chave Pix nulo ou zero",invalidInputException.getMessage());

    }
    @Test
    void RetornarOresponseEntityNoDeletePixCpf(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.deletePixCpf(account),HttpStatus.OK),accountController.deletePixCpf(account));
    }
    @Test
    void retornarPixCellphone(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.pixCellphone(transactionPix));
        Assertions.assertEquals("ID da transação Pix nulo ou zero", invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoPixCellphone(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.pixCellphone(transactionPix), HttpStatus.OK), accountController.pixCellphone(transactionPix));
    }
    @Test
    void retornarTransferPixCellphone(){
        transactionPix.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.transferPixCellphone(transactionPix));
        Assertions.assertEquals("ID da transação Pix nulo ou zero", invalidInputException.getMessage());
    }
    @Test
    void RetornarOresponseEntityNoPixTransferCellphone(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.transferPixCellphone(transactionPix), HttpStatus.OK),accountController.transferPixCellphone(transactionPix));
    }
    @Test
    void retornarOdeletePixCelphone(){
        account.setId(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()-> accountController.deletePixCellPhone(account));
        Assertions.assertEquals("ID da conta responsável pela chave Pix nulo ou zero", invalidInputException.getMessage());

    }
    @Test
    void RetornarOresponseEntityNoPixDeleteCellphone(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.deletePixCellPhone(account),HttpStatus.OK),accountController.deletePixCellPhone(account));
    }
    @Test
    void retornarOpixCnpj(){
    transactionPix.setId(null);
    InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class,()->accountController.transferPixCnpj(transactionPix));
    }
    @Test
    void RetornarOresponseEntityNoPixCnpj(){
        Assertions.assertEquals(new ResponseEntity<>(accountService.transferPixCnpj(transactionPix),HttpStatus.OK),accountController.transferPixCnpj(transactionPix));
    }










}

