package com.fourcamp.linkbank.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ExtractTypeEnum {
    PIX("Pix"),
    TRANSFER("Transferência"),
    DEPOSIT("Depósito"),
    BILL("Boleto"),
    DEBIT("Cartão de débito"),
    CREDIT("Cartão de crédito"),
    MONTHLY_BILL("Fatura do cartão");

    public String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    ExtractTypeEnum(String key){
        this.key = key;
    }
    private static final Map<String, TypeClientEnum> lookup = new HashMap<String, TypeClientEnum>();
    static {
        for(TypeClientEnum x : EnumSet.allOf(TypeClientEnum.class))
            lookup.putIfAbsent(x.getKey(), x);
    }
    public static TypeClientEnum get(String key) {
        return lookup.get(key);

    }
}
