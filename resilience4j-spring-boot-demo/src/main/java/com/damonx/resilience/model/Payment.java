package com.damonx.resilience.model;

public class Payment {
    private String id;
    private double amount;

    public Payment() {}
    public Payment(String id, double amount) {
        this.id = id; this.amount = amount;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}