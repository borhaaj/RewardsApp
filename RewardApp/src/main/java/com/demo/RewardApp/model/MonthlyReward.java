package com.demo.RewardApp.model;

import lombok.Getter;

@Getter
public class MonthlyReward {
    private String month;
    private long points;

    public MonthlyReward(String month, long points) {
        this.month = month;
        this.points = points;
    }
}
