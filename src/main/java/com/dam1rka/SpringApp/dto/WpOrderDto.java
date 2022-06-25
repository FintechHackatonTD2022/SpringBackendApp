package com.dam1rka.SpringApp.dto;


import com.google.gson.JsonObject;

import java.math.BigDecimal;

public class WpOrderDto {
    public Integer merchant_id;
    public Integer currency;
    public BigDecimal amount;
    public String msisdn;
    public String order_id;
    public Object extra_data;

}
