package com.sky.reward.service;

import com.sky.reward.exception.InvalidAccountNumberException;
import com.sky.reward.exception.TechnicalFailureException;

public interface EligibilityService {
	
	public String checkRewardEligibility(String accountNumber) throws TechnicalFailureException, InvalidAccountNumberException;

}
