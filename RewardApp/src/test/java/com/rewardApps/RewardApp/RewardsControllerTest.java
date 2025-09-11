package com.rewardApps.rewardapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rewardApps.rewardapp.controller.RewardsController;
import com.rewardApps.rewardapp.model.CustomerRewardDetails;
import com.rewardApps.rewardapp.model.MonthlyReward;
import com.rewardApps.rewardapp.model.TransactionRequest;
import com.rewardApps.rewardapp.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardsController rewardsController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rewardsController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testCalculateRewards() throws Exception {
        TransactionRequest transaction1 = new TransactionRequest("customer1", 120.00, LocalDate.now());
        TransactionRequest transaction2 = new TransactionRequest("customer2", 80.00, LocalDate.now());
        List<TransactionRequest> transactions = Arrays.asList(transaction1, transaction2);
        MonthlyReward monthlyReward = new MonthlyReward(120.00, "06", 23);
        CustomerRewardDetails rewardDetails1 = new CustomerRewardDetails("customer1", Collections.singletonList(monthlyReward), 0);
        CustomerRewardDetails rewardDetails2 = new CustomerRewardDetails("customer2", Collections.singletonList(monthlyReward), 0);
        List<CustomerRewardDetails> expectedRewardDetails = Arrays.asList(rewardDetails1, rewardDetails2);
        when(rewardService.calculateRewards(anyList())).thenReturn(expectedRewardDetails);
        mockMvc.perform(post("/api/rewards/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("customer1"))
                .andExpect(jsonPath("$[1].customerId").value("customer2"));
        verify(rewardService, times(0)).calculateRewards(transactions);
    }

    @Test
    void testCalculateRewardsEmptyTransactions() throws Exception {
        List<TransactionRequest> transactions = Collections.emptyList();
        List<CustomerRewardDetails> expectedRewardDetails = Collections.emptyList();
        when(rewardService.calculateRewards(anyList())).thenReturn(expectedRewardDetails);
        mockMvc.perform(post("/api/rewards/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(rewardService, times(1)).calculateRewards(transactions);
    }

    @Test
    void testCalculateRewardsInvalidTransactions() throws Exception {
        String invalidTransactionJson = "[{\"customer\": cust}]";
        mockMvc.perform(post("/api/rewards/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTransactionJson))
                .andExpect(status().isBadRequest());
        verify(rewardService, never()).calculateRewards(anyList());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
