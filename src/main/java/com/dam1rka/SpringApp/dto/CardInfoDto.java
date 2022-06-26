package com.dam1rka.SpringApp.dto;

public class CardInfoDto {
    private String pan;
    private String exp_month;
    private String exp_year;
    private String cvc2;


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getCvc2() {
        return cvc2;
    }

    public void setCvc2(String cvc2) {
        this.cvc2 = cvc2;
    }
}
