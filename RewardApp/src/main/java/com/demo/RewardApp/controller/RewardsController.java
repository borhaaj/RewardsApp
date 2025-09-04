package com.demo.RewardApp.controller;

import com.demo.RewardApp.model.*;
import com.demo.RewardApp.service.RewardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);
    private final RewardService rewardService;

    public RewardsController(RewardService rewardService) {
        this.rewardService = rewardService;
    }
    @PostMapping("/calculate")
    public ResponseEntity<List<CustomerRewardDetails>> calculateRewards(
            @Valid @RequestBody List<TransactionRequest> transactions) {
        logger.info("Received request to calculate rewards for {} transactions", transactions.size());
        List<CustomerRewardDetails> summaries = rewardService.calculateRewards(transactions);
        logger.info("Returning reward summaries for {} customers", summaries.size());
        return ResponseEntity.ok(summaries);
    }
}