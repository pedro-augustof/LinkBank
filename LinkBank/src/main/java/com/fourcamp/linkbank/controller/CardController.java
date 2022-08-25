package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.exceptions.AlreadyExistsException;
import com.fourcamp.linkbank.exceptions.BillNotFoundException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.exceptions.NotSupportedLimitException;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Policy;
import com.fourcamp.linkbank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {


    @Autowired
    CardService cardService;

    @PutMapping("/block")
    public ResponseEntity<String> blockCard (@RequestBody Card card){
        boolean block = cardService.block(card);
            if(!block){
                return new ResponseEntity("Não foi possível bloquear o cartão",HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity("Cartão bloqueado", HttpStatus.OK);
            }
    }
    @PutMapping("/unblock")
    public ResponseEntity<String> unblockCard (@RequestBody Card card){
        boolean unblock = cardService.unblock(card);
            if (unblock){
                return new ResponseEntity("Não foi possível desbloquear o cartão" , HttpStatus.BAD_REQUEST);
              }else {
                return new ResponseEntity("Cartão desbloqueado", HttpStatus.OK);
            }
    }
    @PutMapping("/adjustCreditLimit")
    public ResponseEntity<String> adjustCreditLimit (@RequestBody Card card){
            if( card.getMonthlyLimit() == null){
                throw new InvalidInputException("Não foi possível ajustar o limite");
          }
        boolean adjustCreditLimit = cardService.adjustCreditLimit(card, card.getMonthlyLimit());
        if(!adjustCreditLimit ){
                throw new NotSupportedLimitException("Não foi possível ajustar o limite");
            }
        return new ResponseEntity("Limite ajustado", HttpStatus.OK);
    }
    @PutMapping("/adjustDebitLimit")
    public ResponseEntity<String> adjustDebitLimit (@RequestBody Card card){
        boolean adjustDebitLimit = cardService.adjustDebitLimit(card, card.getDailyLimit());
        try {
            if (!adjustDebitLimit || card.getDailyLimit() == null){
                throw new InvalidInputException("Não foi possível ajustar o limite");
            }
        } catch (NullPointerException e){
            throw new InvalidInputException("Não foi possível ajustar o limite");
        }
        return new ResponseEntity("Limite ajustado", HttpStatus.OK);
    }
    @PutMapping("/ChangePassword")
    public ResponseEntity<String> changePassword (@RequestBody Card card) {
        if (String.valueOf(card.getPassword()).length() == 6 || card.getPassword() == null ) {
            boolean changePassword = cardService.changePassword(card, card.getPassword());
            try {
                if (!changePassword) {
                    throw new InvalidInputException("Senha inválida!");
                }
            } catch (NullPointerException e) {
                throw new InvalidInputException("Senha inválida!");
            }
            return new ResponseEntity("Senha alterada com sucesso!", HttpStatus.OK);
        } else {
            throw new InvalidInputException("Não foi possível trocar a senha do cartão, digite 6 números");
        }
    }




    @GetMapping("/getBill")
    public ResponseEntity<Object> getBill (@RequestBody Card card){
        Object bill = cardService.getBill(card);
            if (bill == null) {
                throw new BillNotFoundException("Não foi possível recuperar a fatura");
            } else {
                return new ResponseEntity(cardService.getBill(card), HttpStatus.OK);
            }
    }

    @PostMapping("/insurance")
    public ResponseEntity<String> createInsurance (@RequestBody Card card){
        boolean isCreated = cardService.createInsurance(card);
            if(!isCreated){
                throw new AlreadyExistsException("Não foi possível criar o seguro");
            }
        return new ResponseEntity("Seguro criado", HttpStatus.OK);
    }

    @DeleteMapping("/insurance")
    public ResponseEntity<String> deleteInsurance(@RequestBody Card card){
        try {
            if(card.getNumber() == null){
                throw new InvalidInputException("Número de cartão inválido");
            }
        } catch (NullPointerException e){
            throw new InvalidInputException("Número de cartão inválido");
        }
        return new ResponseEntity(cardService.deleteInsurance(card), HttpStatus.OK);
    }

    @GetMapping("/insurance")
    public ResponseEntity<Policy> getInsurance (@RequestBody Card card){
        try {
            if (card.getNumber() == null){
               throw new InvalidInputException("Número de cartão inválido");
            }
        } catch (NullPointerException e){
            throw new InvalidInputException("Número de cartão inválido");
        }
        return new ResponseEntity(cardService.getInsurance(card), HttpStatus.OK);
    }

    @PutMapping("/pay")
    public ResponseEntity<String> cardPay(@RequestBody Card card, @RequestParam Double value){
            if(card.getNumber() == null || card.getPassword() == null || value == null ){
                throw new InvalidInputException("Número do cartão/senha inválidos");
            }
        return new ResponseEntity(cardService.pay(card, value), HttpStatus.OK);
    }

    @PutMapping("/paybill")
    public ResponseEntity<String> payBill(@RequestBody Card card){
            if (card.getNumber() == null){
                throw new InvalidInputException("Número do cartão inválido");
            }
        return new ResponseEntity(cardService.payBill(card), HttpStatus.OK);
    }

    @PutMapping("/creditpay")
    public ResponseEntity<String> cardPayCredit(@RequestBody Card card, @RequestParam Double value){
        try {
            if (card.getNumber() == null || card.getPassword() == null || value == null) {
               throw new InvalidInputException("Número do cartão/senha inválidos");
            }
        } catch (NullPointerException e) {
             throw new InvalidInputException("Número do cartão/senha inválidos");
        }
        return new ResponseEntity(cardService.creditPay(card, value), HttpStatus.OK);
    }
}

