package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.exceptions.CardNotFoundException;
import com.fourcamp.linkbank.exceptions.PolicyNotFoundException;
import com.fourcamp.linkbank.exceptions.InsufficientLimitException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.exceptions.*;
import com.fourcamp.linkbank.model.*;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.CardRepository;
import com.fourcamp.linkbank.repository.InsuranceRepository;
import com.fourcamp.linkbank.repository.PolicyRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @InjectMocks
    private CardService service;

    @Mock
    CardRepository cardRepository;

    @Mock
    PolicyRepository policyRepository;

    @Mock
    InsuranceRepository insuranceRepository;

    @Mock
    AccountRepository accountRepository;

    private Card card;

    private LocalDate localDate = LocalDate.now();

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        setCard();
    }

    private void setCard() {
        this.card = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
    }

    @Test
    void DeveRetornarTrueQuandoCartÃ£oForBloqueado(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setActive(false);
        when(cardRepository.save(eq(card))).thenReturn(null);
        card.setActive(true);
        Assertions.assertTrue(service.block(card));
    }

    @Test
     void DeveriaJogarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todoBlock(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.block(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarFalseQuandoCartÃ£oJÃ¡EstiverBloqueado(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setActive(false);
        when(cardRepository.save(eq(card))).thenReturn(null);
        card.setActive(false);
        Assertions.assertFalse(service.block(card));
    }

    @Test
    void DeveRetonarTrueQuandoLimiteDeDÃ©bitoForAjustado(){
        Double newLimit = 0.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setDailyLimit(newLimit);
        when(cardRepository.save(eq(card))).thenReturn(null);
        Assertions.assertTrue(service.adjustDebitLimit(card, newLimit));
    }

    @Test
    void DeveJogarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todoAdjustDebitLimit(){
        Double newLimit = 0.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.adjustDebitLimit(card, newLimit));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarFaturaDoMÃªs(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        Assertions.assertEquals(30.0, service.getBill(card));
    }

    @Test
    void DeveJogarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todogetBill(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(null);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.getBill(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarTrueQuandoMudarASenhaNoMetodoChangePassword(){
        Integer newPassword = 241103;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setPassword(newPassword);
        when(cardRepository.save(eq(card))).thenReturn(null);

        card.setPassword(123456);
        Assertions.assertTrue(service.changePassword(card, newPassword));
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todoChangePassword(){
        Integer newPassword = 241103;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.changePassword(card, newPassword));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornaFalsoQuandoASenhaForAMesmaNoMÃ©todoChangePassword(){
        Integer newPassword = 123456;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setPassword(newPassword);
        when(cardRepository.save(eq(card))).thenReturn(null);

        card.setPassword(123456);
        Assertions.assertFalse(service.changePassword(card, newPassword));
    }

    @Test
    void DeveRetornarTrueQuandoOCartÃ£oJÃ¡EstiverAtivoNoMÃ©todoUnblock(){
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(true);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        card.setActive(true);
        Assertions.assertTrue(service.unblock(card));
    }

    @Test
    void DeveRetornarAQuantidadeDeNumerosRequisitada(){
        int quantity = 2;
        Assertions.assertEquals(quantity , service.createDigit(quantity).length());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todoGetInsurance(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.getInsurance(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarMensagemQuandoSeguroNÃ£oEncontrado(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(null);

        Assertions.assertEquals("NÃ£o existe um seguro para esse cartÃ£o", service.getInsurance(card));
    }

    @Test
    void DeveRetornarSeguroNoMÃ©todoGetInsurance(){
        Policy policy = new Policy(1L, "96237811", card, null, 3000.0, "", 30.0);
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(policyRepository.findPolicyByCard(eq(cardRepository.findByNumber("5555 5555 5555 5555")))).thenReturn(policy);

        Assertions.assertEquals(policy, service.getInsurance(card));
    }

    @Test
    void DeveRetornarMensagemQuandoDeletarOSeguro(){
        Insurance insurance = new Insurance(1L, "Nome", "Regras");
        Policy policy = new Policy(1L, "96237811", card, insurance, 3000.0, "", 30.0);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(policy);
        when(insuranceRepository.findById(policy.getInsurance().getInsurance_id())).thenReturn(Optional.of(insurance));

        Assertions.assertEquals("Seguro cancelado com sucesso", service.deleteInsurance(card));
    }

    @Test
    void DeveRetornarMensagemQuandoSeguroNÃ£oForEncontradoNoDeleteInsurance(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(null);
        PolicyNotFoundException policyNotFoundException = Assert.assertThrows(PolicyNotFoundException.class, ()-> service.deleteInsurance(card));
        Assertions.assertEquals("NÃ£o existe seguro para esse cartÃ£o", policyNotFoundException.getMessage());
    }


    @Test
    void DeveRetornarCardNotFoundExceptionNoMÃ©todoAdjustCreditLimit(){
        Double newLimit = 400.00;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.adjustCreditLimit(card, newLimit));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarFalsoAoTentarRequisitarLimiteMaiorDoQueOLimiteBase(){
        Double newLimit = 7000.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        Assertions.assertFalse(service.adjustCreditLimit(card, newLimit));
    }

    @Test
    void DeveRetornarTrueAoAjustarOLimiteMensalPremium(){
        Double newLimit = 5000.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        Assertions.assertTrue(service.adjustCreditLimit(card, newLimit));
    }
    @Test
    void DeveRetornarTrueAoAjustarOLimiteMensalSuper(){
        Double newLimit = 2000.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setCardType("SUPER");
        Assertions.assertTrue(service.adjustCreditLimit(card, newLimit));
    }@Test
    void DeveRetornarTrueAoAjustarOLimiteMensalBasic(){
        Double newLimit = 1000.0;
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setCardType("BASIC");
        Assertions.assertTrue(service.adjustCreditLimit(card, newLimit));
    }


    @Test
    void DeveRetornarUmCartaoPremiumNoMÃ©todoCreateCard(){
        Client client = new Client(1L, "51901520811", "50880937X", "Gabriel Pinto", "24/11/2003", 40000.0, "12345678", "", "Estagiario", null, "11943183432");
        Account account = new Account(1L, "43280-5", "0001", 0.0, client, null, null, localDate, 0.001, 1000.0  );
        Assertions.assertNotNull(service.createCard(account));
        Assertions.assertInstanceOf(Card.class, service.createCard(account));
    }
    @Test
    void DeveRetornarUmCartaoSuperNoMÃ©todoCreateCard(){
        Client client = new Client(1L, "51901520811", "50880937X", "Gabriel Pinto", "24/11/2003", 3000.0, "12345678", "", "Estagiario", null, "11943183432");
        Account account = new Account(1L, "43280-5", "0001", 0.0, client, null, null, localDate, 0.001, 1000.0  );
        Assertions.assertNotNull(service.createCard(account));
        Assertions.assertInstanceOf(Card.class, service.createCard(account));
    }
    @Test
    void DeveRetornarUmCartaoBasicNoMÃ©todoCreateCard(){
        Client client = new Client(1L, "51901520811", "50880937X", "Gabriel Pinto", "24/11/2003", 1500.00, "12345678", "", "Estagiario", null, "11943183432");
        Account account = new Account(1L, "43280-5", "0001", 0.0, client, null, null, localDate, 0.001, 1000.0  );
        Assertions.assertNotNull(service.createCard(account));
        Assertions.assertInstanceOf(Card.class, service.createCard(account));
    }

    @Test
    void DeveRetornarCardNotFoundExceptionNoMÃ©todoCreateInsurance(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.createInsurance(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarFalseSeJaTemSeguro(){
        Policy policy = new Policy(1L, "96237811", card, null, 3000.0, "", 30.0);
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(policy);

        Assertions.assertFalse(service.createInsurance(card));
    }

    @Test
    void DeveRetornarTrueSeSeguroFoiCriadoPremium(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(null);
        Assertions.assertTrue(service.createInsurance(card));
    }
    @Test
    void DeveRetornarTrueSeSeguroFoiCriadoSuper(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(null);
        card.setCardType("SUPER");
        Assertions.assertTrue(service.createInsurance(card));
    }
    @Test
    void DeveRetornarTrueSeSeguroFoiCriadoBasic(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        when(policyRepository.findPolicyByCard(eq(card))).thenReturn(null);
        card.setCardType("BASIC");
        Assertions.assertTrue(service.createInsurance(card));
    }

    @Test
    void VerificarOMÃ©todoCreatePremiumInsurance(){
        CardService mock = mock(CardService.class);
        mock.createPremiumInsurance(card);
        verify(mock, times(1)).createPremiumInsurance(card);
    }
    @Test
    void VerificarOMÃ©todoCreateSuperInsurance(){
        CardService mock = mock(CardService.class);
        mock.createSuperInsurance(card);
        verify(mock, times(1)).createSuperInsurance(card);
    }
    @Test
    void VerificarOMÃ©todoCreateBasicInsurance(){
        CardService mock = mock(CardService.class);
        mock.createBasicInsurance(card);
        verify(mock, times(1)).createBasicInsurance(card);
    }

    @Test
    void DeveRetornarFalseQuandoCartÃ£oForDesbloqueado(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setActive(true);
        when(cardRepository.save(eq(card))).thenReturn(null);
        card.setActive(false);
        Assertions.assertFalse(service.unblock(card));
    }

    @Test
    void DeveriaJogarExceÃ§Ã£oQuandoCartÃ£oNÃ£oEncontradoNoMÃ©todoUnblock(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(false);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class , ()-> service.unblock(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarTrueQuandoCartÃ£oJÃ¡EstiverDesbloqueado(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        card.setActive(false);
        when(cardRepository.save(eq(card))).thenReturn(null);
        card.setActive(true);
        Assertions.assertTrue(service.block(card));
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCardForNuloPay(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(null);
        Account account = new Account(1L, "43280-5", "0001", 0.0, null, null, card, localDate, 0.001, 1000.0  );
        when(accountRepository.findAccountByCard(card)).thenReturn(account);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> service.pay(card,200.00));
        Assertions.assertEquals("Pagamento invÃ¡lido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoASenhaForInvÃ¡lidaPay(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        Card card1 = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123457, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card1);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> service.pay(card,200.00));
        Assertions.assertEquals("Pagamento invÃ¡lido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoLimiteInsuficientePay(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        InsufficientLimitException insufficientLimitException = Assert.assertThrows(InsufficientLimitException.class, ()-> service.pay(card, 27000.00));
        Assertions.assertEquals("Limite insuficiente", insufficientLimitException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCartÃ£oInativoPay(){
        card.setActive(false);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        CardNotActiveException cardNotActiveException = Assert.assertThrows(CardNotActiveException.class, ()-> service.pay(card, 200.00));
        Assertions.assertEquals("CartÃ£o inativo", cardNotActiveException.getMessage());
    }

    @Test
    void DeveRetornarExeceÃ§Ã£oQuandoContaForNulaPay(){
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        when(accountRepository.findAccountByCard(card)).thenReturn(null);
        AccountNotFoundException accountNotFoundException = Assert.assertThrows(AccountNotFoundException.class, ()-> service.pay(card,200.00));
        Assertions.assertEquals("Saldo insuficiente", accountNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCardForNuloCreditPay(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(null);
        Account account = new Account(1L, "43280-5", "0001", 0.0, null, null, card, localDate, 0.001, 1000.0  );
        when(accountRepository.findAccountByCard(card)).thenReturn(account);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> service.creditPay(card,200.00));
        Assertions.assertEquals("Pagamento invÃ¡lido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoASenhaForInvÃ¡lidaCreditPay(){
        when(cardRepository.existsByNumber(eq("5555 5555 5555 5555"))).thenReturn(true);
        Card card1 = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123457, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card1);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> service.creditPay(card,200.00));
        Assertions.assertEquals("Pagamento invÃ¡lido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoLimiteInsuficienteCreditPay(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        InsufficientLimitException insufficientLimitException = Assert.assertThrows(InsufficientLimitException.class, ()-> service.creditPay(card, 7000.00));
        Assertions.assertEquals("Limite insuficiente", insufficientLimitException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCartÃ£oInativoCreditPay(){
        card.setActive(false);
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        CardNotActiveException cardNotActiveException = Assert.assertThrows(CardNotActiveException.class, ()-> service.creditPay(card, 200.00));
        Assertions.assertEquals("CartÃ£o inativo", cardNotActiveException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoCardForNuloPayBill(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(null);
        Account account = new Account(1L, "43280-5", "0001", 0.0, null, null, card, localDate, 0.001, 1000.0  );
        when(accountRepository.findAccountByCard(card)).thenReturn(account);
        CardNotFoundException cardNotFoundException = Assert.assertThrows(CardNotFoundException.class, ()-> service.payBill(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", cardNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarExeceÃ§Ã£oQuandoContaForNulaPayBill(){
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        when(accountRepository.findAccountByCard(card)).thenReturn(null);
        AccountNotFoundException accountNotFoundException = Assert.assertThrows(AccountNotFoundException.class, ()-> service.payBill(card));
        Assertions.assertEquals("NÃ£o foi possÃ­vel encontrar o cartÃ£o", accountNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarExceÃ§Ã£oQuandoSaldoInsuficienteCreditPay(){
        when(cardRepository.findByNumber(eq("5555 5555 5555 5555"))).thenReturn(card);
        Account account = new Account(1L, "43280-5", "0001", 0.0, null, null, card, localDate, 0.001, 1000.0  );
        when(accountRepository.findAccountByCard(card)).thenReturn(account);
        InsufficientBalanceException insufficientBalanceException = Assert.assertThrows(InsufficientBalanceException.class, ()-> service.payBill(card));
        Assertions.assertEquals("Saldo insuficiente", insufficientBalanceException.getMessage());
    }
}
