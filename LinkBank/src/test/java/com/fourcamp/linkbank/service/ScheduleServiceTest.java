package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    CardRepository cardRepository;

    private Card card;
    private Card card2;
    private Account account;
    private Account account2;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        createCard();
        createAccount();
    }
    private void createCard() {
        this.card = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 30.00, 6000.00, "BASIC", 0.00, 155);
        this.card2 = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 30.00, 6000.00, "BASIC", 5000.00, 155);
    }
    private void createAccount() {
        this.account = new Account(1L, "12345-1", "0001",600.0, null, null, null, null, 500.00, 900.00);
        this.account2 = new Account(1L, "12345-1", "0001",600.0, null, null, null, null, 500.00, 450900.00);
    }


    @Test
    void basic(){
        when(cardRepository.save(any())).thenReturn(null);
        when(accountRepository.save(any())).thenReturn(null);

        ArrayList<Card> listaCard = new ArrayList<>();
        listaCard.add(card);
        when(cardRepository.findAll()).thenReturn(listaCard);

        ArrayList<Account> listaAccount = new ArrayList<>();
        listaAccount.add(account);
        when(accountRepository.findAll()).thenReturn(listaAccount);

        scheduleService.reset24h();

        ArgumentCaptor<Card> captorCard = ArgumentCaptor.forClass(card.getClass());
        ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(account.getClass());
        verify(cardRepository).save(captorCard.capture());
        verify(accountRepository).save(captorAccount.capture());

        Assertions.assertEquals(account2.getSavingsAccountBalance(),captorAccount.getValue().getSavingsAccountBalance());
        Assertions.assertEquals(card2.getDailyLimit(),captorCard.getValue().getDailyLimit());



        verify(accountRepository,times(1)).findAll();
        verify(cardRepository,times(1)).findAll();
        verify(accountRepository,times(1)).save(any());
        verify(cardRepository,times(1)).save(any());
    }

    @Test
    void superTest(){
        card.setCardType("SUPER");
        card2.setDailyLimit(10000.0);
        when(cardRepository.save(any())).thenReturn(null);
        when(accountRepository.save(any())).thenReturn(null);

        ArrayList<Card> listaCard = new ArrayList<>();
        listaCard.add(card);
        when(cardRepository.findAll()).thenReturn(listaCard);

        ArrayList<Account> listaAccount = new ArrayList<>();
        listaAccount.add(account);
        when(accountRepository.findAll()).thenReturn(listaAccount);

        scheduleService.reset24h();

        ArgumentCaptor<Card> captorCard = ArgumentCaptor.forClass(card.getClass());
        ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(account.getClass());
        verify(cardRepository).save(captorCard.capture());
        verify(accountRepository).save(captorAccount.capture());

        Assertions.assertEquals(account2.getSavingsAccountBalance(),captorAccount.getValue().getSavingsAccountBalance());
        Assertions.assertEquals(card2.getDailyLimit(),captorCard.getValue().getDailyLimit());



        verify(accountRepository,times(1)).findAll();
        verify(cardRepository,times(1)).findAll();
        verify(accountRepository,times(1)).save(any());
        verify(cardRepository,times(1)).save(any());
    }

    @Test
    void premiumTest(){
        card.setCardType("PREMIUM");
        card2.setDailyLimit(20000.0);
        when(cardRepository.save(any())).thenReturn(null);
        when(accountRepository.save(any())).thenReturn(null);

        ArrayList<Card> listaCard = new ArrayList<>();
        listaCard.add(card);
        when(cardRepository.findAll()).thenReturn(listaCard);

        ArrayList<Account> listaAccount = new ArrayList<>();
        listaAccount.add(account);
        when(accountRepository.findAll()).thenReturn(listaAccount);

        scheduleService.reset24h();

        ArgumentCaptor<Card> captorCard = ArgumentCaptor.forClass(card.getClass());
        ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(account.getClass());
        verify(cardRepository).save(captorCard.capture());
        verify(accountRepository).save(captorAccount.capture());

        Assertions.assertEquals(account2.getSavingsAccountBalance(),captorAccount.getValue().getSavingsAccountBalance());
        Assertions.assertEquals(card2.getDailyLimit(),captorCard.getValue().getDailyLimit());



        verify(accountRepository,times(1)).findAll();
        verify(cardRepository,times(1)).findAll();
        verify(accountRepository,times(1)).save(any());
        verify(cardRepository,times(1)).save(any());
    }
}
