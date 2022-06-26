package com.dam1rka.SpringApp.dto;

import java.math.BigDecimal;

public class OrderDto {
    private String msisdn;
    private Integer currency;
    private BigDecimal amount;
    private Object extraData;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Object getExtraData() {
        return  extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
