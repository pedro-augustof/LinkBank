package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.TransactionPix;
import com.fourcamp.linkbank.model.TransactionTransfer;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    TransactionService transactionService;


    private TransactionTransfer transactionTransfer;

    private TransactionPix transactionPix;

    private Account receiver;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        createTransactionTransfer();
        createTransactionPix();
        createAccount();
    }

    private void createAccount() {
        Account account = new Account(1L, "numero", "agencia", 500.00, null, null, null, null, 0.001, 900.0);
    }

    private void createTransactionPix() {
        TransactionPix transactionPix = new TransactionPix(100.00, "DATE", 1L, "cpf");
    }

    private void createTransactionTransfer() {
        TransactionTransfer transactionTransfer = new TransactionTransfer(100.00, "DATE", 1L, receiver );
    }


    @Test
    void VerificaOMétodoInsertTransactionTransfer(){
        transactionService.insertTransactionTransfer(transactionTransfer);
        verify(transactionService, times(1)).insertTransactionTransfer(transactionTransfer);

    }
    @Test
    void VerificaOMétodoInsertInsertTransactionPix(){
        transactionService.insertTransactionPix(transactionPix);
        verify(transactionService, times(1)).insertTransactionPix(transactionPix);
    }
    @Test
    void VerificaOMétodoReceiveTransactionTransfer(){
        transactionService.receiveTransactionTransfer(transactionTransfer);
        verify(transactionService, times(1)).receiveTransactionTransfer(transactionTransfer);
    }
    @Test
    void VerificaOMétodoReceiveTransactionPix(){
        transactionService.receiveTransactionPix(transactionPix, receiver);
        verify(transactionService, times(1)).receiveTransactionPix(transactionPix, receiver);

    }
}
