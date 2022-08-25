package com.fourcamp.linkbank.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TypeInsuranceEnum {
    BASIC("BASIC"),
    SUPER("SUPER"),
    PREMIUM("PREMIUM");


    public String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    TypeInsuranceEnum(String key){
        this.key = key;
    }
    private static final Map<String, TypeInsuranceEnum> lookup = new HashMap<String, TypeInsuranceEnum>();
    static {
        for(TypeInsuranceEnum x : EnumSet.allOf(TypeInsuranceEnum.class))
            lookup.putIfAbsent(x.getKey(), x);
    }
    public static TypeInsuranceEnum get(String key) {
        return lookup.get(key);

    }
}
