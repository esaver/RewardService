package com.sky.reward.service.impl;

import org.springframework.stereotype.Service;

import com.sky.reward.exception.InvalidAccountNumberException;
import com.sky.reward.exception.TechnicalFailureException;
import com.sky.reward.service.EligibilityService;
/**
 * Dummy implementation of eligibility service;
 * @author user
 *
 */
@Service
public class EligibilityServiceImpl implements EligibilityService {

	public String checkRewardEligibility(String accountNumber) throws TechnicalFailureException, InvalidAccountNumberException {
		
		String eligiliability = "CUSTOMER_INELIGIBLE";
		
		//Mock test accounts for EligibilityService
		switch (accountNumber) {
		case "1111":
			eligiliability = "CUSTOMER_ELIGIBLE";
			break;
		case "2222":
			eligiliability = "CUSTOMER_INELIGIBLE";
			break;
		case "3333":
			throw new InvalidAccountNumberException("The supplied account number is invalid.");
		case "4444":
			throw new TechnicalFailureException("Service technical failure");
		}
		
		return eligiliability;
	}
}
