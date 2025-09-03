package com.demo.RewardApp.model;

import lombok.Getter;

import java.util.List;
@Getter
public class CustomerRewardDetails {
    private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private long totalPoints;

    public CustomerRewardDetails(String customerId, List<MonthlyReward> monthlyRewards, long totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

}
