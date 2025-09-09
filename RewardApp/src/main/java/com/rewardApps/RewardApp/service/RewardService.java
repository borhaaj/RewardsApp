package com.rewardApps.RewardApp.service;

import com.rewardApps.RewardApp.model.CustomerRewardDetails;
import com.rewardApps.RewardApp.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RewardService {
    @Autowired
    private TransactionValidator transactionValidator;
    @Autowired
    private RewardCalculator rewardCalculator;
    @Autowired
    private RewardAggregator rewardAggregator;

    public List<CustomerRewardDetails> calculateRewards(List<TransactionRequest> transactionRequests) {
        transactionValidator.validate(transactionRequests);
        List<TransactionRequest> recentTransactions = filterLastThreeMonths(transactionRequests);
        return rewardAggregator.aggregate(recentTransactions);
    }

    public List<TransactionRequest> filterLastThreeMonths(List<TransactionRequest> transactions) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(threeMonthsAgo))
                .toList();
    }
}
