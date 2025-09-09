package com.rewardApps.RewardApp.service;

import org.springframework.stereotype.Component;

@Component
public class RewardCalculator {
    public long calculatePoints(double amount) {
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
