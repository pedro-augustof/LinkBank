package com.fourcamp.linkbank.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "policy_id", nullable = false)
    private Long policy_id;

    @Column(nullable = false)
    private String policyNumber;

    @OneToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    private Card card;

    @OneToOne
    @JoinColumn(name = "insurance_id", referencedColumnName = "insurance_id")
    private Insurance insurance;

    @Column(nullable = false)
    private Double policyCost;

    @Column
    private String conditions;

    @Column(nullable = false)
    private Double policyValue;

    public Policy() {
    }

    public Policy(Long policy_id, String policyNumber, Card card, Insurance insurance, Double policyCost, String conditions, Double policyValue) {
        this.policy_id = policy_id;
        this.policyNumber = policyNumber;
        this.card = card;
        this.insurance = insurance;
        this.policyCost = policyCost;
        this.conditions = conditions;
        this.policyValue = policyValue;
    }

    public Double getPolicyValue() {
        return policyValue;
    }

    public void setPolicyValue(Double policyValue) {
        this.policyValue = policyValue;
    }

    public Long getPolicy_id() {
        return policy_id;
    }

    public void setPolicy_id(Long policy_id) {
        this.policy_id = policy_id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Card  getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public Double getPolicyCost() {
        return policyCost;
    }

    public void setPolicyCost(Double policyCost) {
        this.policyCost = policyCost;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
