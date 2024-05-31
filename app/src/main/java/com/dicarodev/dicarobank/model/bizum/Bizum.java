package com.dicarodev.dicarobank.model.bizum;

import com.dicarodev.dicarobank.model.contact.Contact;

import java.io.Serializable;

public class Bizum implements Serializable {
    private double amount;
    private String message;
    private Contact contact;

    public Bizum() {
    }

    public Bizum(double amount, String message, Contact contact) {
        this.amount = amount;
        this.message = message;
        this.contact = contact;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
