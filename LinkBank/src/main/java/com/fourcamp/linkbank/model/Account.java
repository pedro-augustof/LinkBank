package com.fourcamp.linkbank.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Builder;

@Entity
@Builder
public class Account {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(nullable = false, unique = true)
   private String number;

   @Column(nullable = false, length = 4)
   private String agency;

   @Column
   private Double balance;

   @OneToOne
   @JoinColumn(name = "client_id", referencedColumnName = "client_id")
   private Client client;

   @OneToOne
   @JoinColumn(name = "pix_id", referencedColumnName = "pix_id")
   private Pix pix;

   @OneToOne
   @JoinColumn(name = "card_id", referencedColumnName = "card_id")
   private Card card;
   @Column(nullable = false)
   private LocalDate data;
   @Column()
   private Double savingsAccountRate;
   @Column
   private Double savingsAccountBalance;
   

   public Account(Long id, String number, String agency, Double balance, Client client, Pix pix, Card card, LocalDate data, Double savingsAccountRate, Double savingsAccountBalance) {
      this.id = id;
      this.number = number;
      this.agency = agency;
      this.balance = balance;
      this.client = client;
      this.pix = pix;
      this.card = card;
      this.data = data;
      this.savingsAccountRate = savingsAccountRate;
      this.savingsAccountBalance = savingsAccountBalance;
   }

   public Account() {
   }

   public LocalDate getData() {
      return data;
   }

   public void setData(LocalDate data) {
      this.data = data;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNumber() {
      return number;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public String getAgency() {
      return agency;
   }

   public void setAgency(String agency) {
      this.agency = agency;
   }

   public Double getBalance() {
      return balance;
   }

   public void setBalance(Double balance) {
      this.balance = balance;
   }

   public Client getClient() {
      return client;
   }

   public void setClient(Client client) {
      this.client = client;
   }

   public Pix getPix() {
      return pix;
   }

   public void setPix(Pix pix) {
      this.pix = pix;
   }

   public Card getCard() {
      return card;
   }

   public void setCard(Card card) {
      this.card = card;
   }

   public Double getSavingsAccountRate() {
      return savingsAccountRate;
   }

   public void setSavingsAccountRate(Double savingsAccountRate) {
      this.savingsAccountRate = savingsAccountRate;
   }

   public Double getSavingsAccountBalance() {
      return savingsAccountBalance;
   }

   public void setSavingsAccountBalance(Double savingsAccountBalance) {
      this.savingsAccountBalance = savingsAccountBalance;
   }
}
