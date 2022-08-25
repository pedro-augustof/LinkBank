package com.fourcamp.linkbank.model;

import com.fourcamp.linkbank.enums.TypeInsuranceEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "insurance_id", nullable = false)
    private Long insurance_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String rules;

    public Insurance() {
    }

    public Insurance(Long insurance_id, String name, String rules) {
        this.insurance_id = insurance_id;
        this.name = name;
        this.rules = rules;
    }

    public Long getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(Long insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
