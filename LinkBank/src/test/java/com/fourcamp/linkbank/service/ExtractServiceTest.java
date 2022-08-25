package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Extract;
import com.fourcamp.linkbank.repository.ExtractRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ExtractServiceTest {

    @InjectMocks
    ExtractService extractService;

    @Mock
    ExtractRepository extractRepository;

    private Extract extract;

    private Account account;



    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        createExtract();
        createAccount();

    }

    private void createAccount() {
        this.account = new Account(1L, "12345-1", "0001",600.0, null, null, null, null, 500.00, 900.00);
    }


    private void createExtract() {
        this.extract = new Extract(1L,"pix",500.00,"pago",1L);
    }


    @Test
    void retornarAlistaDeExtratos(){
        List<Extract> extracts = new ArrayList<>();
        extracts.add(extract);
        when(extractRepository.findExtractByAccount_id(eq(1L))).thenReturn((extracts));
        Assertions.assertEquals(extracts, extractService.listById(1L));

    }
    @Test
    void InserirDeposito(){
        ExtractService mock = Mockito.mock(ExtractService.class);
        mock.insertDeposit(account,500.00);
        verify(mock, times(1)).insertDeposit(account,500.00);

    }

    @Test
    void insertCreditPay(){
        ExtractService mock = Mockito.mock(ExtractService.class);
        mock.insertCreditPay(account, 200.00);
        verify(mock, times(1)).insertCreditPay(account,200.00);
    }
    @Test
    void insertMonthlyBill(){
        ExtractService mock = Mockito.mock(ExtractService.class);
        mock.insertMonthlyBill(account,300.00);
        verify(mock, times(1)).insertMonthlyBill(account,300.00);
    }
    @Test
    void insertCardPay(){
        ExtractService mock = Mockito.mock(ExtractService.class);
        mock.insertCardPay(account, 100.00);
        verify(mock,times(1)).insertCardPay(account,100.00);
    }
    @Test
    void insertBill(){
        ExtractService mock = Mockito.mock(ExtractService.class);
        mock.insertBill(account, 900.00);
        verify(mock,times(1)).insertBill(account,900.00);
    }



}
