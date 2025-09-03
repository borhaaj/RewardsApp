package com.demo.RewardApp;

import com.demo.RewardApp.model.TransactionRequest;
import com.demo.RewardApp.exception.InvalidTransactionException;
import com.demo.RewardApp.service.RewardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RewardServiceTest {
    @InjectMocks
    private RewardService rewardService;

    @Test
    void testCalculateRewards_SingleTransaction() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 120.0, LocalDate.of(2023, 1, 15))
        );

        var result = rewardService.calculateRewards(transactions);

        assertEquals(1, result.size());
        assertEquals("C001", result.get(0).getCustomerId());
        assertEquals(90, result.get(0).getTotalPoints());
        assertEquals(1, result.get(0).getMonthlyRewards().size());
        assertEquals("2023-01", result.get(0).getMonthlyRewards().get(0).getMonth());
        assertEquals(90, result.get(0).getMonthlyRewards().get(0).getPoints());
    }

    @Test
    void testCalculateRewards_MultipleTransactions_SameMonth() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 120.0, LocalDate.of(2023, 1, 15)),
                new TransactionRequest("C001", 75.0, LocalDate.of(2023, 1, 20))
        );

        var result = rewardService.calculateRewards(transactions);

        assertEquals(1, result.size());
        assertEquals("C001", result.get(0).getCustomerId());
        assertEquals(115, result.get(0).getTotalPoints());
        assertEquals(1, result.get(0).getMonthlyRewards().size());
        assertEquals("2023-01", result.get(0).getMonthlyRewards().get(0).getMonth());
        assertEquals(115, result.get(0).getMonthlyRewards().get(0).getPoints());
    }

    @Test
    void testCalculateRewards_MultipleTransactions_DifferentMonths() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 120.0, LocalDate.of(2023, 1, 15)),
                new TransactionRequest("C001", 150.0, LocalDate.of(2023, 2, 10))
        );

        var result = rewardService.calculateRewards(transactions);

        assertEquals(1, result.size());
        assertEquals("C001", result.get(0).getCustomerId());
        assertEquals(480, result.get(0).getTotalPoints());
        assertEquals(2, result.get(0).getMonthlyRewards().size());
        assertEquals("2023-01", result.get(0).getMonthlyRewards().get(0).getMonth());
        assertEquals(240, result.get(0).getMonthlyRewards().get(0).getPoints());
        assertEquals("2023-02", result.get(0).getMonthlyRewards().get(1).getMonth());
        assertEquals(240, result.get(0).getMonthlyRewards().get(1).getPoints());
    }

    @Test
    void testCalculateRewards_ZeroAmount_ShouldThrowException() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 0.0, LocalDate.of(2023, 1, 15))
        );

        assertThrows(InvalidTransactionException.class, () -> {
            rewardService.calculateRewards(transactions);
        });
    }

    @Test
    void testCalculateRewards_FutureDate_ShouldThrowException() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 50.0, LocalDate.now().plusDays(1))
        );

        assertThrows(InvalidTransactionException.class, () -> {
            rewardService.calculateRewards(transactions);
        });
    }

    @Test
    void testCalculateRewards_EdgeCase_50Dollars() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 50.0, LocalDate.of(2023, 1, 15))
        );

        var result = rewardService.calculateRewards(transactions);

        assertEquals(0, result.get(0).getTotalPoints());
    }

    @Test
    void testCalculateRewards_EdgeCase_100Dollars() {
        List<TransactionRequest> transactions = List.of(
                new TransactionRequest("C001", 100.0, LocalDate.of(2023, 1, 15))
        );

        var result = rewardService.calculateRewards(transactions);

        assertEquals(50, result.get(0).getTotalPoints());
    }
}
