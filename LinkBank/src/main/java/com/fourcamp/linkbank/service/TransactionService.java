package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.enums.ExtractEnum;
import com.fourcamp.linkbank.enums.ExtractTypeEnum;
import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Extract;
import com.fourcamp.linkbank.model.TransactionPix;
import com.fourcamp.linkbank.model.TransactionTransfer;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.ExtractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    ExtractRepository extractRepository;

    @Autowired
    AccountRepository accountRepository;

    public void insertTransactionTransfer(TransactionTransfer transaction){
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.TRANSFER.getKey() + " para " +
                transaction.getReceiverAccount().getClient().getFullname());

        extract.setValue(transaction.getValue());
        extract.setAccount_id(transaction.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }

    public void insertTransactionPix(TransactionPix transaction){
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.PIX.getKey() + " para " +
                transaction.getPixKey());

        extract.setValue(transaction.getValue());
        extract.setAccount_id(transaction.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }

    public void receiveTransactionTransfer(TransactionTransfer transactionTransfer){
        Extract extract = new Extract();

        Optional<Account> account = accountRepository.findById(transactionTransfer.getId());
        Account payer = account.get();

        extract.setDescription(ExtractTypeEnum.TRANSFER.getKey() + " recebida de " + payer.getClient().getFullname());
        extract.setValue(transactionTransfer.getValue());

        Account receiver = accountRepository.findAccountByNumber(transactionTransfer.getReceiverAccount().getNumber());

        extract.setAccount_id(receiver.getId());
        extract.setType(ExtractEnum.RECIEVED.getKey());

        extractRepository.save(extract);
    }

    public void receiveTransactionPix(TransactionPix transactionPix, Account receiver){
        Extract extract = new Extract();

        Optional<Account> account = accountRepository.findById(transactionPix.getId());
        Account payer = account.get();

        extract.setDescription(ExtractTypeEnum.PIX.getKey() + " recebido de " + payer.getClient().getFullname());
        extract.setValue(transactionPix.getValue());

        extract.setAccount_id(receiver.getId());
        extract.setType(ExtractEnum.RECIEVED.getKey());

        extractRepository.save(extract);
    }
}
