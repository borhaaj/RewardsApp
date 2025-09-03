package com.demo.RewardApp.service;

import com.demo.RewardApp.exception.InvalidTransactionException;
import com.demo.RewardApp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RewardService {
    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public List<CustomerRewardDetails> calculateRewards(List<TransactionRequest> transactionRequests) {
        logger.info("Processing {} transactions", transactionRequests.size());

        validateTransactions(transactionRequests);

        Map<String, Map<String, List<TransactionRequest>>> customerMonthlyTransactions = new ConcurrentHashMap<>();
        Map<String, List<TransactionRequest>> txMap = new ConcurrentHashMap<>();
        List<TransactionRequest> txList= new ArrayList<>();
        transactionRequests.forEach(t -> {
            String month = t.getDate().format(MONTH_FORMATTER);
            customerMonthlyTransactions
                    .computeIfAbsent(t.getCustomerId(), k -> txMap)
                    .computeIfAbsent(month, k ->txList)
                    .add(t);
        });

        // Calculate rewards
        List<CustomerRewardDetails> summaries = customerMonthlyTransactions.entrySet().stream()
                .map(entry -> {
                    String customerId = entry.getKey();
                    List<MonthlyReward> monthlyRewards = new ArrayList<>();
                    long totalPoints = 0;

                    for (Map.Entry<String, List<TransactionRequest>> monthlyEntry : entry.getValue().entrySet()) {
                        String month = monthlyEntry.getKey();
                        long monthlyPoints = monthlyEntry.getValue().stream()
                                .mapToLong(t -> calculatePoints(t.getAmount()))
                                .sum();

                        monthlyRewards.add(new MonthlyReward(month, monthlyPoints));
                        totalPoints += monthlyPoints;
                    }

                    // Sort by month
                    monthlyRewards.sort((a, b) -> YearMonth.parse(a.getMonth(), MONTH_FORMATTER)
                            .compareTo(YearMonth.parse(b.getMonth(), MONTH_FORMATTER)));

                    return new CustomerRewardDetails(customerId, monthlyRewards, totalPoints);
                })
                .collect(Collectors.toList());

        logger.info("Processed rewards for {} customers", summaries.size());
        return summaries;
    }

    private void validateTransactions(List<TransactionRequest> transactions) {
        for (TransactionRequest t : transactions) {
            if (t.getAmount() <= 0) {
                throw new InvalidTransactionException("Transaction amount must be greater than 0");
            }
            if (t.getDate().isAfter(LocalDate.now())) {
                throw new InvalidTransactionException("Transaction date cannot be in the future");
            }
        }
    }
    private long calculatePoints(double amount) {
        long points = 0;
        if (amount > 100) {
            points += (long)((amount - 100) * 2);
            amount = 100;
        }
        if (amount > 50) {
            points += (long)(amount - 50);
        }
        return points;
    }

}