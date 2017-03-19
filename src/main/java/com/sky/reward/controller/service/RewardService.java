package com.sky.reward.controller.service;

import java.util.List;

import com.sky.reward.controller.exceptions.InvalidAccountNumberException;
import com.sky.reward.controller.exceptions.TechnicalFailureException;
import com.sky.reward.domain.Reward;

public interface RewardService {
	public List<String> fetchRewards(String accountNumber, List<String> subscriptions) throws TechnicalFailureException, InvalidAccountNumberException;
}
