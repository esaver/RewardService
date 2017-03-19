package com.sky.reward.controller.service;

import com.sky.reward.controller.exceptions.InvalidAccountNumberException;
import com.sky.reward.controller.exceptions.TechnicalFailureException;

public interface EligibilityService {
	
	public String checkRewardEligibility(String accountNumber) throws TechnicalFailureException, InvalidAccountNumberException;

}
