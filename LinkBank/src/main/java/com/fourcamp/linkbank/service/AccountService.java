package com.fourcamp.linkbank.service;

import java.util.NoSuchElementException;
import java.util.Optional;


import com.fourcamp.linkbank.exceptions.AccountNotFoundException;
import com.fourcamp.linkbank.exceptions.ClientNotFoundException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.model.Pix;

import com.fourcamp.linkbank.dto.Login;
import com.fourcamp.linkbank.model.*;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.ClientRepository;
import com.fourcamp.linkbank.repository.ExtractRepository;
import com.fourcamp.linkbank.repository.PixRepository;
import com.fourcamp.linkbank.utils.Validations;


@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PixRepository pixRepository;

    @Autowired
    ExtractRepository extractRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ExtractService extractService;

    public Account login(Login login) {
        Client client = clientRepository.findClientByCpf(login.getCpf());

        if (client == null) {
            throw new ClientNotFoundException();
        }

        if (client.getPassword().equals(login.getPassword())) {
            return accountRepository.findAccountByClient(client);
        } else {
            throw new InvalidInputException("Senha inválida");
        }
    }

    public Account loginresetpassword(Login login) {
        Client client = clientRepository.findClientByCpf(login.getCpf());

        if (client == null) {
            throw new ClientNotFoundException();
        }

        if (client.getPassword().equals(login.getPassword())) {
            throw new InvalidInputException("Senha igual");
        } else {
            client.setPassword(login.getPassword());
            clientRepository.save(client);
            return accountRepository.findAccountByClient(client);
        }
    }

    public String deposit(Account deposit) {
        Optional<Account> optAccount = Optional.ofNullable(accountRepository.findAccountByNumber(deposit.getNumber()));

        if (optAccount.isEmpty()) {
            throw new AccountNotFoundException();
        } else {
            Account account = optAccount.get();

            account = appendBalance(account, deposit.getBalance());

            extractService.insertDeposit(account, deposit.getBalance());

            accountRepository.save(account);

            return "Depósito realizado com sucesso";
        }
    }

    public String depositInSavingsAccount(Account deposit) {
        Optional<Account> optAccount = Optional.ofNullable(accountRepository.findAccountByNumber(deposit.getNumber()));

        if (optAccount.isEmpty()) {
            throw new AccountNotFoundException();
        } else {
            Account account = optAccount.get();

            account = appendSavingsBalance(account, deposit.getSavingsAccountBalance());

            extractService.insertDeposit(account, deposit.getSavingsAccountBalance());

            accountRepository.save(account);

            return "Depósito realizado com sucesso";
        }
    }

    public String payBill(Account bill) {
        Optional<Account> optAccount = accountRepository.findById(bill.getId());

        try {
            Pix pix = optAccount.get().getPix();
            if (pix == null) {
                return "Pagamento inválido";
            }
        } catch (NoSuchElementException e) {
            return "Pagamento inválido";
        }

        Account account = discountBalance(optAccount.get(), bill.getBalance());

        try {
            if (account == null) {
                return "Saldo insuficiente";
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }

        extractService.insertBill(account, bill.getBalance());
        accountRepository.save(account);

        return "Boleto pago com sucesso!";
    }

    public String transfer(TransactionTransfer transactionTransfer) {

        Optional<Account> optAccount = accountRepository.findById(transactionTransfer.getId());


        try {
            Pix pix = optAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta nao existe!";
        }

        Account account = optAccount.get();
        Account reciever = transactionTransfer.getReceiverAccount();


        if (reciever.getAgency().length() != 4 || !Validations.isAccountNumber(reciever.getNumber())) {
            return "Informações da conta inválidos";
        }

        try {
            account = discountBalanceWithTax(account, transactionTransfer.getValue());

            if (account == null) {
                return "Saldo insuficiente";
            } else {
                accountRepository.save(account);
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }

        transactionService.insertTransactionTransfer(transactionTransfer);

        if (reciever.getAgency().equals("0001")) {

            reciever = accountRepository.findAccountByNumber(reciever.getNumber());

            try {
                if (reciever.getBalance() == null) {
                    return "Transação concluída com sucesso!";
                }
            } catch (NullPointerException e) {
                return "Transação concluída com sucesso!";
            }

            reciever = appendBalance(reciever, transactionTransfer.getValue());

            transactionService.receiveTransactionTransfer(transactionTransfer);

            accountRepository.save(reciever);
        }

        return "Transação concluída com sucesso!";
    }

    public String pixEmail(TransactionPix transactionPix) {

        Optional<Account> account = accountRepository.findById(transactionPix.getId());

        Pix pix = new Pix();

        try {
            pix = account.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }

        try {
            pixRepository.findByEmail(transactionPix.getPixKey()).getEmail();
        } catch (NullPointerException e) {
            pix.setEmail(transactionPix.getPixKey());
            accountRepository.save(account.get());
            return "Chave pix cadastrada com sucesso!";
        }

        return "Não foi possível cadastrar";
    }

    public String deletePixEmail(Account id) {
        Optional<Account> account = accountRepository.findById(id.getId());

        Pix pix;

        try {
            pix = account.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }

        Account newAccount = account.get();

        newAccount.getPix().setEmail(null);

        accountRepository.save(newAccount);

        return "Pix deletado com sucesso!";
    }

    public String transferPixEmail(TransactionPix transactionPix) {
        Optional<Account> optAccount = accountRepository.findById(transactionPix.getId());

        Pix pix;

        try {
            pix = optAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }

        Account account = optAccount.get();

        try {
            account = discountBalance(account, transactionPix.getValue());

            if (account == null) {
                return "Saldo insuficiente";
            } else {
                accountRepository.save(account);
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }

        transactionService.insertTransactionPix(transactionPix);

        pix = pixRepository.findByEmail(transactionPix.getPixKey());
        account = accountRepository.findAccountByPix(pix);

        try {
            account = appendSavingsBalance(account, transactionPix.getValue());
            transactionService.receiveTransactionPix(transactionPix, account);
            accountRepository.save(account);

        } catch (NullPointerException e) {

        }

        return "Transferência feita com sucesso!";
    }

    public String pixCellphone(TransactionPix transactionPix) {

        Optional<Account> account = accountRepository.findById(transactionPix.getId());

        Pix pix = new Pix();

        try {
            pix = account.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        try {
            pixRepository.findByCellphone(transactionPix.getPixKey()).getCellphone();
        } catch (NullPointerException e) {
            pix.setCellphone(transactionPix.getPixKey());
            accountRepository.save(account.get());
            return "Chave pix cadastrada com sucesso!";
        }

        return "Não foi possível cadastrar";
    }

    public String transferPixCellphone(TransactionPix transactionPix) {

        Optional<Account> secAccount = accountRepository.findById(transactionPix.getId());

        Pix pix;

        try {
            pix = secAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        Account account = secAccount.get();

        try {
            account = discountBalance(account, transactionPix.getValue());
            if (account == null) {
                return "Saldo insuficiente";
            } else {
                accountRepository.save(account);
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }
        transactionService.insertTransactionPix(transactionPix);
        pix = pixRepository.findByCellphone(transactionPix.getPixKey());

        account = accountRepository.findAccountByPix(pix);

        try {
            account = appendBalance(account, transactionPix.getValue());
            transactionService.receiveTransactionPix(transactionPix, account);
            accountRepository.save(account);
        } catch (NullPointerException e) {

        }

        return "Transferência feita com sucesso";
    }

    public String deletePixCellPhone(Account id) {

        Optional<Account> secAccount = accountRepository.findById(id.getId());

        Pix pix;

        try {
            pix = secAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        Account account = secAccount.get();
        account.getPix().setCellphone(null);
        accountRepository.save(account);

        return "Pix deletado com sucesso";
    }

    public String pixCpf(TransactionPix transactionPix) {

        Optional<Account> account = accountRepository.findById(transactionPix.getId());

        Pix pix = new Pix();

        try {
            pix = account.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }

        try {
            pixRepository.findByCpf(transactionPix.getPixKey()).getCpf();
        } catch (NullPointerException e) {
            pix.setCpf(transactionPix.getPixKey());
            accountRepository.save(account.get());
            return "Chave pix cadastrada com sucesso!";
        }
        return "Não foi possível cadastrar";
    }

    public String transferPixCpf(TransactionPix transactionPix) {

        Optional<Account> secAccount = accountRepository.findById(transactionPix.getId());

        Pix pix;

        try {
            pix = secAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        Account account = secAccount.get();
        try {
            account = discountBalance(account, transactionPix.getValue());
            if (account == null) {
                return "Saldo insuficiente";
            } else {
                accountRepository.save(account);
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }
        transactionService.insertTransactionPix(transactionPix);
        pix = pixRepository.findByCpf(transactionPix.getPixKey());

        account = accountRepository.findAccountByPix(pix);

        try {
            account = appendBalance(account, transactionPix.getValue());
            transactionService.receiveTransactionPix(transactionPix, account);
            accountRepository.save(account);
        } catch (NullPointerException e) {

        }
        return "Transferência feita com sucesso";
    }

    public String deletePixCpf(Account id) {

        Optional<Account> secAccount = accountRepository.findById(id.getId());

        Pix pix;

        try {
            pix = secAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        Account account = secAccount.get();
        account.getPix().setCpf(null);
        accountRepository.save(account);

        return "Pix deletado com sucesso";
    }

    public String transferPixCnpj(TransactionPix transactionPix) {

        Optional<Account> secAccount = accountRepository.findById(transactionPix.getId());

        Pix pix;

        try {
            pix = secAccount.get().getPix();
        } catch (NoSuchElementException e) {
            return "Conta não existe!";
        }
        Account account = secAccount.get();
        try {
            account = discountBalance(account, transactionPix.getValue());
            if (account == null) {
                return "Saldo insuficiente";
            } else {
                accountRepository.save(account);
            }
        } catch (NullPointerException e) {
            return "Saldo insuficiente";
        }
        transactionService.insertTransactionPix(transactionPix);

        return "Transferência feita com sucesso";
    }

    public Double getBalance(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        try {
            return account.get().getBalance();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Double getSavingsAccountBalance(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        try {
            return account.get().getSavingsAccountBalance();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Account discountBalanceWithTax(Account account, Double value) {
        Double transferRate = 3.5;

        if (account.getBalance() < (value + transferRate)) {
            return null;
        }

        account.setBalance(account.getBalance() - value - transferRate);

        return account;

    }

    public Account discountBalance(Account account, Double value) {
        if (account.getBalance() < value) {
            return null;
        }

        account.setBalance(account.getBalance() - value);

        return account;

    }


    public Account appendBalance(Account account, Double value) {
        account.setBalance(account.getBalance() + value);

        return account;
    }

    public Account appendSavingsBalance(Account account, Double value) {
        account.setSavingsAccountBalance(account.getSavingsAccountBalance() + value);

        return account;
    }
}
