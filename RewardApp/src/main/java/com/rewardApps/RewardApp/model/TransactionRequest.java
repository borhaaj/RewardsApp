package com.rewardApps.rewardApp.model;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransactionRequest {
    private final String customerId;
    private final double amount;
    private final LocalDate date;

    public TransactionRequest(String customerId, double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }
}
