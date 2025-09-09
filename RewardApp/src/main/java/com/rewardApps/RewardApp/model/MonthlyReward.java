package com.rewardApps.RewardApp.model;

import lombok.Getter;

@Getter
public class MonthlyReward {
    private double amount;
    private String month;
    private long points;

    public MonthlyReward(double amount, String month, long points) {
        this.amount = amount;
        this.month = month;
        this.points = points;
    }
}
