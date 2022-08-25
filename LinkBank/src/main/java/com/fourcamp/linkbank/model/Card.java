package com.fourcamp.linkbank.model;


import javax.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id", nullable = false)
    private Long card_id;

    @Column(nullable = false, unique = true)
    protected String number;

    @Column(nullable = false)
    private String flag;

    @Column
    protected Integer password;

    @Column(nullable = false)
    protected Boolean isActive;

    @Column
    protected Double monthlyBill;

    @Column(nullable = false)
    private Double monthlyLimit;

    @Column
    private String cardType;

    @Column(nullable = false)
    private Double dailyLimit;

    @Column(nullable = false)
    private Integer cvv;


    public Long getId() {
        return card_id;
    }

    public void setId(Long id) {
        this.card_id = id;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    public Double getMonthlyBill() {
        return monthlyBill;
    }

    public void setMonthlyBill(Double monthlyBill) {
        this.monthlyBill = monthlyBill;
    }

    public Double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Card() {
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Card(Long id, String number, String flag, Integer password, Boolean isActive, Double monthlyBill, Double monthlyLimit, String cardType, Double dailyLimit, Integer cvv) {
        this.card_id = id;
        this.number = number;
        this.flag = flag;
        this.password = password;
        this.isActive = isActive;
        this.monthlyBill = monthlyBill;
        this.monthlyLimit = monthlyLimit;
        this.cardType = cardType;
        this.dailyLimit = dailyLimit;
        this.cvv = cvv;
    }
}
