package com.rewardApps.rewardapp.service;

import com.rewardApps.rewardapp.exception.InvalidTransactionException;
import com.rewardApps.rewardapp.model.TransactionRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TransactionValidator {
    public void validate(List<TransactionRequest> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new InvalidTransactionException("Transaction list cannot be null or empty");
        }
        for (TransactionRequest t : transactions) {
            if (t.getAmount() <= 0) throw new InvalidTransactionException("Amount must be > 0");
            if (t.getDate().isAfter(LocalDate.now())) throw new InvalidTransactionException("Date cannot be in future");
        }
    }
}
