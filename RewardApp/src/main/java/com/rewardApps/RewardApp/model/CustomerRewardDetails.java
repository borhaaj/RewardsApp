package com.rewardApps.rewardApp.model;

import lombok.Getter;

import java.util.List;

@Getter
public class CustomerRewardDetails {
    private final String customerId;
    private final List<MonthlyReward> monthlyRewards;
    private final long totalPoints;

    public CustomerRewardDetails(String customerId, List<MonthlyReward> monthlyRewards, long totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

}
