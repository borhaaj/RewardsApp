package com.rewardApps.RewardApp.service;

import com.rewardApps.RewardApp.model.CustomerRewardDetails;
import com.rewardApps.RewardApp.model.MonthlyReward;
import com.rewardApps.RewardApp.model.TransactionRequest;
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
        Map<String, Map<String, List<TransactionRequest>>> grouped = new HashMap<>();

        for (TransactionRequest t : transactions) {
            String customerId = t.getCustomerId();
            String month = t.getDate().format(MONTH_FORMATTER);
            grouped.computeIfAbsent(customerId, k -> new HashMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .add(t);
        }

        return grouped.entrySet().stream()
                .map(entry -> {
                    String customerId = entry.getKey();
                    List<MonthlyReward> monthlyRewards = new ArrayList<>();
                    long totalPoints = 0;
                    for (Map.Entry<String, List<TransactionRequest>> monthEntry : entry.getValue().entrySet()) {
                        String month = monthEntry.getKey();
                        List<TransactionRequest> txns = monthEntry.getValue();
                        long monthlyPoints = txns.stream()
                                .mapToLong(t -> calculator.calculatePoints(t.getAmount()))
                                .sum();
                        double totalAmount = txns.stream()
                                .mapToDouble(TransactionRequest::getAmount)
                                .sum();
                        monthlyRewards.add(new MonthlyReward(totalAmount, month, monthlyPoints));
                        totalPoints += monthlyPoints;
                    }
                    monthlyRewards.sort(Comparator.comparing(m -> YearMonth.parse(m.getMonth(), MONTH_FORMATTER)));
                    return new CustomerRewardDetails(customerId, monthlyRewards, totalPoints);
                })
                .collect(Collectors.toList());
    }
}
