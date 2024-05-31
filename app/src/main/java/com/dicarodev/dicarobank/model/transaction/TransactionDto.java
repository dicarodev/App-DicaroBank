package com.dicarodev.dicarobank.model.transaction;

import java.io.Serializable;

public class TransactionDto implements Serializable {
    private String transactionDate;
    private double amount;
    private String detail;
    private String originAccountNumber;
    private String destinyAccountNumber;

    public TransactionDto(String transactionDate, double amount, String detail, String originAccountNumber, String destinyAccountNumber) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.detail = detail;
        this.originAccountNumber = originAccountNumber;
        this.destinyAccountNumber = destinyAccountNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
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

    public String getOriginAccountNumber() {
        return originAccountNumber;
    }

    public void setOriginAccountNumber(String originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }

    public String getDestinyAccountNumber() {
        return destinyAccountNumber;
    }

    public void setDestinyAccountNumber(String destinyAccountNumber) {
        this.destinyAccountNumber = destinyAccountNumber;
    }
}
