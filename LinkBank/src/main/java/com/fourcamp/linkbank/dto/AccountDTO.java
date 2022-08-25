package com.fourcamp.linkbank.dto;

import java.time.LocalDate;

import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.model.Pix;

public class AccountDTO {

	private Long id;
	private String number;
	private String agency;
	private Double balance;
	private Client client;
	private Pix pix;
	private Card card;
	private LocalDate data;
	private Double savingsAccountRate;
	private Double savingsAccountBalance;

}
