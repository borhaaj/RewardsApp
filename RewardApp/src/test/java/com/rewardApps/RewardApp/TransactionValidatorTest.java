package com.rewardApps.rewardApp;

import com.rewardApps.rewardApp.exception.InvalidTransactionException;
import com.rewardApps.rewardApp.model.TransactionRequest;
import com.rewardApps.rewardApp.service.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionValidatorTest {

    private TransactionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TransactionValidator();
    }

    @Test
    void testValidTransactionsShouldPass() {
        List<TransactionRequest> transactions = Arrays.asList(
                new TransactionRequest("C001", 100.0, LocalDate.now().minusDays(1)),
                new TransactionRequest("C002", 50.0, LocalDate.now().minusDays(5))
        );

        assertDoesNotThrow(() -> validator.validate(transactions));
    }

    @Test
    void testNullTransactionListThrowException() {
        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> validator.validate(null)
        );
        assertEquals("Transaction list cannot be null or empty", ex.getMessage());
    }

    @Test
    void testEmptyTransactionListThrowException() {
        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> validator.validate(Collections.emptyList())
        );
        assertEquals("Transaction list cannot be null or empty", ex.getMessage());
    }

    @Test
    void testNegativeAmountThrowException() {
        List<TransactionRequest> transactions = Arrays.asList(
                new TransactionRequest("C001", -10.0, LocalDate.now().minusDays(1))
        );

        InvalidTransactionException ex = assertThrows(
                InvalidTransactionException.class,
                () -> validator.validate(transactions)
        );
        assertEquals("Amount must be > 0", ex.getMessage());
    }
}
