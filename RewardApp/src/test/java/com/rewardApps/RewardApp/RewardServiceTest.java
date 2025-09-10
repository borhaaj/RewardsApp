package com.rewardApps.rewardApp;

import com.rewardApps.rewardApp.model.CustomerRewardDetails;
import com.rewardApps.rewardApp.model.TransactionRequest;
import com.rewardApps.rewardApp.service.RewardAggregator;
import com.rewardApps.rewardApp.service.RewardCalculator;
import com.rewardApps.rewardApp.service.RewardService;
import com.rewardApps.rewardApp.service.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionValidator transactionValidator;

    @Mock
    private RewardCalculator rewardCalculator;

    @Mock
    private RewardAggregator rewardAggregator;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void calculateRewards_shouldHandleEmptyTransactions() {
        List<TransactionRequest> transactions = List.of();
        doNothing().when(transactionValidator).validate(transactions);
        List<CustomerRewardDetails> expectedRewards = List.of();
        when(rewardAggregator.aggregate(List.of())).thenReturn(expectedRewards);
        List<CustomerRewardDetails> result = rewardService.calculateRewards(transactions);
        verify(transactionValidator).validate(transactions);
        verify(rewardAggregator).aggregate(List.of());
        verifyNoInteractions(rewardCalculator);
        assertEquals(expectedRewards, result);
    }

    @Test
    void calculateRewards_shouldThrowExceptionWhenValidationFails() {
        List<TransactionRequest> transactions = List.of(new TransactionRequest("C001", 100.0,LocalDate.now()));
        doThrow(new IllegalArgumentException("Invalid transaction")).when(transactionValidator).validate(transactions);
        assertThrows(IllegalArgumentException.class, () -> rewardService.calculateRewards(transactions));
        verifyNoInteractions(rewardAggregator, rewardCalculator);
    }

    @Test
    void filterLastThreeMonths_shouldFilterCorrectly() {
        LocalDate today = LocalDate.now();
        TransactionRequest recent1 = new TransactionRequest("C001", 100.0,LocalDate.now());
        TransactionRequest recent2 = new TransactionRequest("C002", 100.0,LocalDate.now());
        TransactionRequest boundary = new TransactionRequest("C003", 100.0,LocalDate.now());
        TransactionRequest old = new TransactionRequest("C004", 100.0,LocalDate.now());
        List<TransactionRequest> transactions = List.of(recent1, recent2, boundary, old);
        List<TransactionRequest> filtered = rewardService.filterLastThreeMonths(transactions);
        assertEquals(4, filtered.size());
        assertTrue(filtered.contains(recent1));
        assertTrue(filtered.contains(recent2));
        assertTrue(filtered.contains(boundary));
    }
}