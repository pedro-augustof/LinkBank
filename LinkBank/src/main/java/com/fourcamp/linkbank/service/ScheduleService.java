package com.fourcamp.linkbank.service;


import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@EnableScheduling
public class ScheduleService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    private static final String TIME_ZONE = "America/Sao_Paulo";


    @Scheduled(cron = "0 0 0 1 * *", zone = TIME_ZONE)
    public void reset24h() {
        accountRepository.findAll().forEach(account ->{
            Double profit = account.getSavingsAccountBalance() * account.getSavingsAccountRate();
            Double newBalance = profit + account.getSavingsAccountBalance();
            account.setSavingsAccountBalance(newBalance);
            accountRepository.save(account);
                });

        cardRepository.findAll().forEach(card -> {
            if(Objects.equals(card.getCardType(), "BASIC")){
                card.setDailyLimit(5000.00);
            } else if(Objects.equals(card.getCardType(), "SUPER")){
                card.setDailyLimit(10000.00);
            } else {
                card.setDailyLimit(20000.00);
            }
            cardRepository.save(card);
        });

    }
    }




