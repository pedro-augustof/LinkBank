package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.enums.TypeClientEnum;
import com.fourcamp.linkbank.enums.TypeInsuranceEnum;
import com.fourcamp.linkbank.exceptions.AccountNotFoundException;
import com.fourcamp.linkbank.exceptions.CardNotFoundException;
import com.fourcamp.linkbank.exceptions.PolicyNotFoundException;
import com.fourcamp.linkbank.exceptions.*;
import com.fourcamp.linkbank.model.*;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.CardRepository;
import com.fourcamp.linkbank.repository.InsuranceRepository;
import com.fourcamp.linkbank.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class CardService {


    @Autowired
    CardRepository cardRepository;

    @Autowired
    PolicyRepository policyRepository;

    @Autowired
    InsuranceRepository insuranceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ExtractService extractService;

    public Boolean block(Card card) {
        Boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Card card1 = cardRepository.findByNumber(card.getNumber());
            if (!card1.getActive()) {
                return false;
            } else {
                card1.setActive(false);
                cardRepository.save(card1);
                return true;
            }
        }
    }

    public Boolean unblock(Card card) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Card card1 = cardRepository.findByNumber(card.getNumber());
            if (card1.getActive()) {
                return true;
            } else {
                card1.setActive(true);
                cardRepository.save(card1);
                return false;
            }
        }

    }


    public boolean changePassword(Card card, Integer newPassword) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Card card1 = cardRepository.findByNumber(card.getNumber());
            if (Objects.equals(newPassword, card1.getPassword())) {
                return false;
            } else {
                card1.setPassword(newPassword);
                cardRepository.save(card1);
                return true;
            }
        }
    }


    public Object getBill(Card card) {
        Card card1 = cardRepository.findByNumber(card.getNumber());
        if (card1 == null) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        }
        return card1.getMonthlyBill();
    }


    public String createDigit(int quantity) {
        String digits = "";

        Random random = new Random();
        for (int x = 0; x < quantity; x++) {
            int number = random.nextInt(0, 10);
            digits += Integer.toString(number);

        }
        return digits;
    }

    public Card createCard(Account account) {
        Card card = new Card();
        if (account.getClient().getIncome() <= 2000) {
            card.setCardType(TypeClientEnum.BASIC.getKey());
            card.setMonthlyLimit(1500.00);
            card.setDailyLimit(5000.00);
        } else if (account.getClient().getIncome() <= 5000 && account.getClient().getIncome() >= 2001) {
            card.setCardType(TypeClientEnum.SUPER.getKey());
            card.setMonthlyLimit(3000.00);
            card.setDailyLimit(10000.00);
        } else {
            card.setCardType(TypeClientEnum.PREMIUM.getKey());
            card.setMonthlyLimit(6000.00);
            card.setDailyLimit(20000.00);
        }
        String cardNumber = createDigit(4) + " " + createDigit(4) + " " + createDigit(4) + " " + createDigit(4);
        card.setNumber(cardNumber);
        Integer cvv = Integer.valueOf(createDigit(3));
        card.setCvv(cvv);
        String flag = "MASTERCARD";
        card.setFlag(flag);
        card.setActive(true);
        card.setMonthlyBill(0.00);

        cardRepository.save(card);
        return card;
    }

    public Boolean adjustCreditLimit(Card card, Double newLimit) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Card card1 = cardRepository.findByNumber(card.getNumber());
            double baseLimit = 0;

            if (Objects.equals(card1.getCardType(), "PREMIUM")) {
                baseLimit = 6000.00;
            } else if (Objects.equals(card1.getCardType(), "SUPER")) {
                baseLimit = 3000.00;
            } else {
                baseLimit = 1500.00;
            }
            if (newLimit > baseLimit) {
                return false;
            } else {
                card1.setMonthlyLimit(newLimit);
                cardRepository.save(card1);
                return true;
            }
        }
    }


    public Boolean adjustDebitLimit(Card card, Double newLimit) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Card card1 = cardRepository.findByNumber(card.getNumber());
            card1.setDailyLimit(newLimit);
            cardRepository.save(card1);
            return true;

        }
    }

    public boolean createInsurance(Card card) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        }
        Card card1 = cardRepository.findByNumber(card.getNumber());
        Policy policy = policyRepository.findPolicyByCard(card1);
        if (policy != null) {
            return false;
        } else {
            if (Objects.equals(card1.getCardType(), "BASIC")) {
                createBasicInsurance(card1);
                return true;
            } else if (Objects.equals(card.getCardType(), "SUPER")) {
                createSuperInsurance(card1);
                return true;
            } else {
                createPremiumInsurance(card1);
                return true;
            }
        }
    }


    public void createBasicInsurance(Card card) {
        Insurance insurance = new Insurance();
        insurance.setName(TypeInsuranceEnum.BASIC.getKey());
        insurance.setRules("TER NO MINIMO UMA CONTA DO TIPO BASIC NO LINKBANK");
        insuranceRepository.save(insurance);
        Policy policy = new Policy();
        policy.setCard(card);
        String policyNumber = createDigit(8);
        policy.setPolicyNumber(policyNumber);
        Double policyCost = 20.00;
        policy.setPolicyCost(policyCost);
        policy.setInsurance(insurance);
        policy.setConditions("cobre apenas roubo/furto");
        policy.setPolicyValue(3000.00);
        policyRepository.save(policy);
    }

    public void createSuperInsurance(Card card) {
        Insurance insurance = new Insurance();
        insurance.setName(TypeInsuranceEnum.SUPER.getKey());
        insurance.setRules("TER NO MINIMO UMA CONTA DO TIPO SUPER NO LINKBANK");
        insuranceRepository.save(insurance);
        Policy policy = new Policy();
        policy.setCard(card);
        String policyNumber = createDigit(8);
        policy.setPolicyNumber(policyNumber);
        Double policyCost = 40.00;
        policy.setPolicyCost(policyCost);
        policy.setInsurance(insurance);
        policy.setConditions("cobre roubo/furto e perda");
        policy.setPolicyValue(8000.00);
        policyRepository.save(policy);
    }

    public void createPremiumInsurance(Card card) {
        Insurance insurance = new Insurance();
        insurance.setName(TypeInsuranceEnum.PREMIUM.getKey());
        insurance.setRules("TER UMA CONTA DO TIPO PREMIUM NO LINKBANK");
        insuranceRepository.save(insurance);
        Policy policy = new Policy();
        policy.setCard(card);
        String policyNumber = createDigit(8);
        policy.setPolicyNumber(policyNumber);
        Double policyCost = 60.00;
        policy.setPolicyCost(policyCost);
        policy.setInsurance(insurance);
        policy.setConditions("cobre roubo/furto, perda e invalidez total/morte por acidente");
        policy.setPolicyValue(13000.00);
        policyRepository.save(policy);
    }


    public String deleteInsurance(Card card) {
        Card card1 = cardRepository.findByNumber(card.getNumber());
        Policy policy = policyRepository.findPolicyByCard(card1);
        if (policy == null) {
            throw new PolicyNotFoundException("Não existe seguro para esse cartão");
        } else {
            Optional<Insurance> optInsurance = insuranceRepository.findById(policy.getInsurance().getInsurance_id());
            Insurance insurance = optInsurance.get();
            policyRepository.delete(policy);
            insuranceRepository.delete(insurance);
            return "Seguro cancelado com sucesso";
        }
    }

    public Object getInsurance(Card card) {
        boolean verify = cardRepository.existsByNumber(card.getNumber());
        if (!verify) {
            throw new CardNotFoundException("Não foi possível encontrar o cartão");
        } else {
            Policy policy = policyRepository.findPolicyByCard(cardRepository.findByNumber(card.getNumber()));
            return Objects.requireNonNullElse(policy, "Não existe um seguro para esse cartão");
        }
    }

    public String pay(Card card, Double valuetopay) {
        Card card1 = cardRepository.findByNumber(card.getNumber());
        Account account = accountRepository.findAccountByCard(card1);

        try {

            if (card1 == null || !Objects.equals(card.getPassword(), card1.getPassword())) {
                throw new InvalidInputException("Pagamento inválido");
            }
        } catch (NullPointerException e) {
            throw new InvalidInputException("Pagamento inválido");
        }
        Double value = valuetopay + (valuetopay * 0.01);
        if (card1.getDailyLimit() < value) {
            throw new InsufficientLimitException("Limite insuficiente");
        } else if (!card1.getActive()) {
            throw new CardNotActiveException("Cartão inativo");
        }
        card1.setDailyLimit(card1.getDailyLimit() - value);

        if (account == null) {
            throw new AccountNotFoundException("Saldo insuficiente");
        }
        account = accountService.discountBalance(account, value);


        account = accountService.discountBalance(account, value);

        extractService.insertCardPay(account, value);
        cardRepository.save(card1);
        accountRepository.save(account);
        return "Pagamento concluído com sucesso!";
    }


    public String creditPay(Card card, Double valuetopay) {
        Card card1 = cardRepository.findByNumber(card.getNumber());
        Account account = accountRepository.findAccountByCard(card1);
        try {

            if (card1 == null || !Objects.equals(card.getPassword(), card1.getPassword())) {
                throw new InvalidInputException("Pagamento inválido");
            }
        } catch (NullPointerException e) {
            throw new InvalidInputException("Pagamento inválido");
        }
        Double value = valuetopay + (valuetopay * 0.01);
        if (card1.getMonthlyLimit() < value + card1.getMonthlyBill()) {
            throw new InsufficientLimitException("Limite insuficiente");
        } else if (!card1.getActive()) {
            throw new CardNotActiveException("Cartão inativo");
        }
        card1.setMonthlyBill(card1.getMonthlyBill() + value);
        extractService.insertCreditPay(account, value);
        cardRepository.save(card1);
        accountRepository.save(account);

        return "Pagamento concluído com sucesso!";
    }

    public String payBill(Card card) {
        Card card1 = cardRepository.findByNumber(card.getNumber());
        Account account = accountRepository.findAccountByCard(card1);

        try {
            if (card1 == null) {
                throw new CardNotFoundException("Não foi possível encontrar o cartão");
            } else if (account == null) {
                throw new AccountNotFoundException("Não foi possível encontrar o cartão");
            }
        } catch (NullPointerException e) {
            throw new AccountNotFoundException("Não foi possível encontrar o cartão");
        }

        if (account.getBalance() < account.getCard().getMonthlyBill()) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }

        account.setBalance(account.getBalance() - account.getCard().getMonthlyBill());


        extractService.insertMonthlyBill(account, account.getCard().getMonthlyBill());

        account.getCard().setMonthlyBill(0.0);

        cardRepository.save(account.getCard());
        accountRepository.save(account);


        return "Fatura paga com sucesso";
    }
}

