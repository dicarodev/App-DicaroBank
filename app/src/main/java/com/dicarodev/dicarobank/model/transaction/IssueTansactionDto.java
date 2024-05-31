package com.dicarodev.dicarobank.model.transaction;

public class IssueTansactionDto {
    private double amount;
    private String detail;
    private String destinyAccountNumber;

    public IssueTansactionDto() {
    }

    public IssueTansactionDto(double amount, String detail, String destinyAccountNumber) {
        this.amount = amount;
        this.detail = detail;
        this.destinyAccountNumber = destinyAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDestinyAccountNumber() {
        return destinyAccountNumber;
    }

    public void setDestinyAccountNumber(String destinyAccountNumber) {
        this.destinyAccountNumber = destinyAccountNumber;
    }
}
