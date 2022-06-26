package com.dam1rka.SpringApp.dto;

public class GetCardResponseDto {
    private String encrypted;
    private String code;
    private String message;

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String token) {
        this.encrypted = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
