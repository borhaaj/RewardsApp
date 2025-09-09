package com.rewardApps.RewardApp.model;


import lombok.Getter;

import java.time.LocalDate;
@Getter
public class TransactionRequest {
    private String customerId;
    private double amount;
    private LocalDate date;

    public TransactionRequest(String customerId, double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }
}
