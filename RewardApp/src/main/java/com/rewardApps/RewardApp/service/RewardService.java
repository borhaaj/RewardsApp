package com.rewardApps.rewardapp.service;

import com.rewardApps.rewardapp.model.CustomerRewardDetails;
import com.rewardApps.rewardapp.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RewardService {
    private final TransactionValidator transactionValidator;
    private final RewardAggregator rewardAggregator;

    @Autowired
    public RewardService(TransactionValidator transactionValidator,
                         RewardAggregator rewardAggregator) {
        this.transactionValidator = transactionValidator;
        this.rewardAggregator = rewardAggregator;
    }

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
