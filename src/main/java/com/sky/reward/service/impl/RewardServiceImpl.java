package com.sky.reward.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.reward.exception.InvalidAccountNumberException;
import com.sky.reward.exception.TechnicalFailureException;
import com.sky.reward.service.EligibilityService;
import com.sky.reward.service.RewardService;

@Service
public class RewardServiceImpl implements RewardService {

	@SuppressWarnings("serial")
	public static final Map<String, List<String>> rewardsMap = new HashMap<String, List<String>>() {
		{
			put("SPORTS", Arrays.asList("CHAMPIONS_LEAGUE_FINAL_TICKET"));
			put("MUSIC", Arrays.asList("KARAOKE_PRO_MICROPHONE"));
			put("MOVIES", Arrays.asList("PIRATES_OF_THE_CARIBBEAN_COLLECTION"));
		}
	};

	@Autowired
	private EligibilityService eligibilityService;

	public List<String> fetchRewards(String accountNumber, List<String> subscriptions) throws TechnicalFailureException, InvalidAccountNumberException {

		String eligibility = eligibilityService.checkRewardEligibility(accountNumber);
		List<String> rewards = new ArrayList<String>();

		if (eligibility.equals("CUSTOMER_ELIGIBLE")) {
			rewards = getRewards(subscriptions);
		}

		return rewards;
	}

	protected List<String> getRewards(List<String> subscriptions) {
		List<String> rewards = new ArrayList<String>();

		for (String subscription : subscriptions) {
			if(rewardsMap.containsKey(subscription)){
				rewards.addAll(rewardsMap.get(subscription));
			}
		}

		return rewards;
	}
}
