package com.dicarodev.dicarobank.model.bizum;

public class BizumDto {
    private double amount;
    private String detail;

    public BizumDto() {
    }

    public BizumDto(double amount, String detail) {
        this.amount = amount;
        this.detail = detail;
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
}
