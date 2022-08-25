package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.dto.ErrorDescription;
import com.fourcamp.linkbank.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDescription handleInvalidInput(InvalidInputException ex){
        return new ErrorDescription(BAD_REQUEST.value(), ex.getMessage(), new Date());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleAlreadyExistsException(AlreadyExistsException ex){
        return new ErrorDescription(BAD_REQUEST.value(), ex.getMessage(), new Date());
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleCardNotFoundException(CardNotFoundException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleCardNotFoundException(AccountNotFoundException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }
    @ExceptionHandler(NotSupportedLimitException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleNotSupportedLimitException(NotSupportedLimitException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }
    @ExceptionHandler(BillNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleBillNotFoundException(BillNotFoundException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }
    @ExceptionHandler(PolicyNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDescription handleInput(PolicyNotFoundException ex){
        return new ErrorDescription(BAD_REQUEST.value(),ex.getMessage(), new Date());
    }
    @ExceptionHandler(InsufficientLimitException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleInsufficientLimitException(InsufficientLimitException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }

    @ExceptionHandler(CardNotActiveException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleCardNotActiveException(CardNotActiveException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorDescription handleInsufficientBalanceException(InsufficientBalanceException exception){
        return new ErrorDescription(BAD_REQUEST.value(), exception.getMessage(), new Date());
    }
}
