package com.rewardApps.rewardApp;

import com.rewardApps.rewardApp.model.CustomerRewardDetails;
import com.rewardApps.rewardApp.model.MonthlyReward;
import com.rewardApps.rewardApp.model.TransactionRequest;
import com.rewardApps.rewardApp.service.RewardAggregator;
import com.rewardApps.rewardApp.service.RewardCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardAggregatorTest {

    private RewardCalculator calculator;
    private RewardAggregator aggregator;

    @BeforeEach
    void setUp() {
        calculator = mock(RewardCalculator.class);
        aggregator = new RewardAggregator(calculator);
    }

    @Test
    void testAggregateRewardsByCustomerAndMonth() {
        TransactionRequest tx1 = new TransactionRequest("C001",120.0, LocalDate.of(2023, 7, 15) );
        TransactionRequest tx2 = new TransactionRequest("C001", 80.0,LocalDate.of(2023, 7, 20));
        TransactionRequest tx3 = new TransactionRequest("C001", 200.0,LocalDate.of(2023, 8, 5) );
        TransactionRequest tx4 = new TransactionRequest("C002", 50.0,LocalDate.of(2023, 7, 10) );
        List<TransactionRequest> transactions = Arrays.asList(tx1, tx2, tx3, tx4);
        when(calculator.calculatePoints(120.0)).thenReturn(90L);
        when(calculator.calculatePoints(80.0)).thenReturn(30L);
        when(calculator.calculatePoints(200.0)).thenReturn(150L);
        when(calculator.calculatePoints(50.0)).thenReturn(0L);
        List<CustomerRewardDetails> result = aggregator.aggregate(transactions);
        assertEquals(2, result.size());
        CustomerRewardDetails cust1 = result.stream()
                .filter(r -> r.getCustomerId().equals("CUST001"))
                .findFirst()
                .orElseThrow();
        assertEquals(2, cust1.getMonthlyRewards().size());
        assertEquals(270L, cust1.getTotalPoints());
        MonthlyReward julyReward = cust1.getMonthlyRewards().get(0);
        assertEquals("2023-07", julyReward.getMonth());
        assertEquals(120L, julyReward.getPoints());
        MonthlyReward augustReward = cust1.getMonthlyRewards().get(1);
        assertEquals("2023-08", augustReward.getMonth());
        assertEquals(150L, augustReward.getPoints());
        CustomerRewardDetails cust2 = result.stream()
                .filter(r -> r.getCustomerId().equals("CUST002"))
                .findFirst()
                .orElseThrow();
        assertEquals(1, cust2.getMonthlyRewards().size());
        assertEquals(0L, cust2.getTotalPoints());
    }
}
