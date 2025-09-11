package com.rewardApps.rewardapp.model;

import lombok.Getter;

@Getter
public class MonthlyReward {
    private final double amount;
    private final String month;
    private final long points;

    public MonthlyReward(double amount, String month, long points) {
        this.amount = amount;
        this.month = month;
        this.points = points;
    }
}
