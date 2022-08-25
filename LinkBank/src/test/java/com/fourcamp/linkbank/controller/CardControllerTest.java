package com.fourcamp.linkbank.controller;



import com.fourcamp.linkbank.exceptions.*;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.service.CardService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


public class CardControllerTest {

    @InjectMocks
    CardController cardController;

    @Mock
    CardService cardService;

    private Card card;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        setCard();
    }
    private void setCard() {
        this.card = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
    }

    @Test
    void DeveRetornarResponseEntityOkSeCartãoForBloqueado(){
        when(cardService.block(eq(card))).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Cartão bloqueado", HttpStatus.OK), cardController.blockCard(card));
        Mockito.verify(cardService, times(1)).block(card);
    }

    @Test
    void DeveRetornarResponseEntityBadRequestSeCartãoNãoForBloqueado(){
        when(cardService.block(eq(card))).thenReturn(false);
        Assertions.assertEquals(new ResponseEntity("Não foi possível bloquear o cartão",HttpStatus.BAD_REQUEST), cardController.blockCard(card));
        Mockito.verify(cardService, times(1)).block(card);
    }
    @Test
    void DeveRetornarResponseEntityOkSeCartãoForDesbloqueado(){
        when(cardService.unblock(eq(card))).thenReturn(false);
        Assertions.assertEquals(new ResponseEntity("Cartão desbloqueado", HttpStatus.OK), cardController.unblockCard(card));
        Mockito.verify(cardService, times(1)).unblock(card);
    }

    @Test
    void DeveRetornarResponseEntityBadRequestSeCartãoNãoForDesbloqueado(){
        when(cardService.unblock(eq(card))).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Não foi possível desbloquear o cartão" , HttpStatus.BAD_REQUEST), cardController.unblockCard(card));
        Mockito.verify(cardService, times(1)).unblock(card);
    }

    @Test
    void DeveRetornarExcecaoQuandoLimiteForMaiorQueOPermitido(){
        when(cardService.adjustCreditLimit(card, 50000.0)).thenReturn(false);
        NotSupportedLimitException notSupportedLimitException = Assert.assertThrows(NotSupportedLimitException.class, ()-> cardController.adjustCreditLimit(card));
        Assertions.assertEquals("Não foi possível ajustar o limite", notSupportedLimitException.getMessage());
        Mockito.verify(cardService, times(1)).adjustCreditLimit(card, card.getMonthlyLimit());
    }
    @Test
    void DeveRetornarExcecaoQuandoLimiteForNullQueOPermitido(){
        card.setMonthlyLimit(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.adjustCreditLimit(card));
        Assertions.assertEquals("Não foi possível ajustar o limite", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarResponseEntityQuandoLimiteForAjustado(){
        when(cardService.adjustCreditLimit(card, card.getMonthlyLimit())).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Limite ajustado", HttpStatus.OK), cardController.adjustCreditLimit(card));
    }

    @Test
    void DeveRetornarResponseEntityQuandoLimiteForAjustadoDébito(){
        when(cardService.adjustDebitLimit(card, card.getDailyLimit())).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Limite ajustado", HttpStatus.OK), cardController.adjustDebitLimit(card));
    }


    @Test
    void DeveRetornarExceçãoSeBillForNull(){
        when(cardService.getBill(card)).thenReturn(null);
        BillNotFoundException billNotFoundException = Assert.assertThrows(BillNotFoundException.class, ()-> cardController.getBill(card));
        Assertions.assertEquals("Não foi possível recuperar a fatura", billNotFoundException.getMessage());
    }

    @Test
    void DeveRetornarBillNoMétodoGetBill(){
        when(cardService.getBill(card)).thenReturn(card.getMonthlyBill());
        Assertions.assertEquals(new ResponseEntity(cardService.getBill(card), HttpStatus.OK), cardController.getBill(card));
    }

    @Test
    void DeveRetornarExceçãoQuandoNaoForPossivelCriarSeguro(){
        when(cardService.createInsurance(card)).thenReturn(false);
        AlreadyExistsException alreadyExistsException = Assert.assertThrows(AlreadyExistsException.class, ()-> cardController.createInsurance(card));
        Assertions.assertEquals("Não foi possível criar o seguro", alreadyExistsException.getMessage());
    }

    @Test
    void DeveRetornarResponseEntityQuandoSeguroCriado(){
        when(cardService.createInsurance(card)).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Seguro criado", HttpStatus.OK), cardController.createInsurance(card));
    }

    @Test
    void DeveRetonarExceçãoQuandoCartaoForNuloNoDeleteInsurance(){
        card = null;
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.deleteInsurance(card));
        Assertions.assertEquals("Número de cartão inválido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetonarExceçãoQuandoNumeroDoCartaoForNuloNoDeleteInsurance(){
        card.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.deleteInsurance(card));
        Assertions.assertEquals("Número de cartão inválido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarMensagemSeSeguroCancelado(){
        Assertions.assertEquals(new ResponseEntity(cardService.deleteInsurance(card), HttpStatus.OK), cardController.deleteInsurance(card));
    }

    @Test
    void DeveRetonarExceçãoQuandoCartaoForNuloNoGetInsurance(){
        card = null;
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.getInsurance(card));
        Assertions.assertEquals("Número de cartão inválido", invalidInputException.getMessage());
    }
    @Test
    void DeveRetonarExceçãoQuandoNumeroDoCartaoForNuloNoGetInsurance(){
        card.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.getInsurance(card));
        Assertions.assertEquals("Número de cartão inválido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarSeguroNoGetInsurance(){
        Assertions.assertEquals(new ResponseEntity(cardService.getInsurance(card), HttpStatus.OK), cardController.getInsurance(card));
    }

    @Test
    void DeveRetornarInvalidInputExceptionNoMétodoCardPayQuandoNumeroForNull(){
        Double value = 20.0;
        card.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.cardPay(card, value));
        Assertions.assertEquals("Número do cartão/senha inválidos", invalidInputException.getMessage());
    }
    @Test
    void DeveRetornarInvalidInputExceptionNoMétodoCardPayQuandoSenhaForNull(){
        Double value = 20.0;
        card.setPassword(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.cardPay(card, value));
        Assertions.assertEquals("Número do cartão/senha inválidos", invalidInputException.getMessage());
    }
    @Test
    void DeveRetornarInvalidInputExceptionNoMétodoCardPayQuandoValorForNull(){
        Double value = null;
        card.setPassword(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.cardPay(card, value));
        Assertions.assertEquals("Número do cartão/senha inválidos", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarMensagemDeSucessoNoMétodoCardPay(){
        Double value = 20.0;
        Assertions.assertEquals(new ResponseEntity(cardService.pay(card, value), HttpStatus.OK), cardController.cardPay(card, value));
    }


    @Test
    void DeveRetornarInvalidInputExceptionQuandoCardForNullNoMétodoPayBill(){
        card.setNumber(null);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, ()-> cardController.payBill(card));
        Assertions.assertEquals("Número do cartão inválido", invalidInputException.getMessage());
    }

    @Test
    void DeveRetornarResponseEntityOkEMensagemNoMétodoPayBill(){
        Assertions.assertEquals(new ResponseEntity(cardService.payBill(card), HttpStatus.OK), cardController.payBill(card));
    }

    @Test
    void DeveRetornarMensagemDeSucessoNoMétodoCardPayCredit(){
        Double value = 20.0;
        Assertions.assertEquals(new ResponseEntity(cardService.creditPay(card, value), HttpStatus.OK), cardController.cardPayCredit(card, value));
    }

    @Test
    void DeveRetornarResponseEntityOkEMensagemNoMétodoChangePassword(){
        Card card1 = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123458, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
        when(cardService.changePassword(card1, card1.getPassword())).thenReturn(true);
        Assertions.assertEquals(new ResponseEntity("Senha alterada com sucesso!", HttpStatus.OK), cardController.changePassword(card1));
    }


}
