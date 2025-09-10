package com.rewardApps.rewardApp.service;

import com.rewardApps.rewardApp.model.CustomerRewardDetails;
import com.rewardApps.rewardApp.model.MonthlyReward;
import com.rewardApps.rewardApp.model.TransactionRequest;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RewardAggregator {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private final RewardCalculator calculator;

    public RewardAggregator(RewardCalculator calculator) {
        this.calculator = calculator;
    }

    public List<CustomerRewardDetails> aggregate(List<TransactionRequest> transactions) {
        Map<String, Map<String, List<TransactionRequest>>> groupOfTx = new HashMap<>();

        for (TransactionRequest t : transactions) {
            String customerId = t.getCustomerId();
            String month = t.getDate().format(MONTH_FORMATTER);
            groupOfTx.computeIfAbsent(customerId, k -> new HashMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .add(t);
        }
        return groupOfTx.entrySet().stream()
                .map(entry -> buildCustomerRewardDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private CustomerRewardDetails buildCustomerRewardDetails(String customerId, Map<String, List<TransactionRequest>> monthlyTxnMap) {
        List<MonthlyReward> monthlyRewards = monthlyTxnMap.entrySet().stream()
                .map(this::calculateMonthlyReward)
                .sorted(Comparator.comparing(r -> YearMonth.parse(r.getMonth(), MONTH_FORMATTER)))
                .collect(Collectors.toList());

        long totalPoints = monthlyRewards.stream()
                .mapToLong(MonthlyReward::getPoints)
                .sum();

        return new CustomerRewardDetails(customerId, monthlyRewards, totalPoints);
    }

    private MonthlyReward calculateMonthlyReward(Map.Entry<String, List<TransactionRequest>> monthEntry) {
        String month = monthEntry.getKey();
        List<TransactionRequest> transactions = monthEntry.getValue();

        long monthlyPoints = transactions.stream()
                .mapToLong(t -> calculator.calculatePoints(t.getAmount()))
                .sum();

        double totalAmount = transactions.stream()
                .mapToDouble(TransactionRequest::getAmount)
                .sum();

        return new MonthlyReward(totalAmount, month, monthlyPoints);
    }

}
