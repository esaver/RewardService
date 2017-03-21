package com.sky.reward.service;

import java.util.List;

import com.sky.reward.exception.InvalidAccountNumberException;
import com.sky.reward.exception.TechnicalFailureException;

public interface RewardService {
	public List<String> fetchRewards(String accountNumber, List<String> subscriptions) throws TechnicalFailureException, InvalidAccountNumberException;
}
