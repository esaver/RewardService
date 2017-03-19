package com.sky.reward.controller.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.reward.controller.exceptions.InvalidAccountNumberException;
import com.sky.reward.controller.exceptions.TechnicalFailureException;
import com.sky.reward.controller.service.EligibilityService;
import com.sky.reward.controller.service.RewardService;

@Service
public class RewardServiceImpl implements RewardService {

	@SuppressWarnings("serial")
	public static final Map<String, String> rewardsMap = new HashMap<String, String>() {
		{
			put("SPORTS", "CHAMPIONS_LEAGUE_FINAL_TICKET");
			put("MUSIC", "KARAOKE_PRO_MICROPHONE");
			put("MOVIES", "PIRATES_OF_THE_CARIBBEAN_COLLECTION");
		}
	};

	@Autowired
	private EligibilityService eligibilityService;

	public List<String> fetchRewards(String accountNumber, List<String> subscriptions) throws TechnicalFailureException, InvalidAccountNumberException {

		String eligibility = eligibilityService.checkRewardEligibility(accountNumber);
		List<String> rewards = new ArrayList<>();

		if (eligibility.equals("CUSTOMER_ELIGIBLE")) {
			rewards = getRewards(subscriptions);
		}

		return rewards;
	}

	protected List<String> getRewards(List<String> subscriptions) {
		List<String> rewards = new ArrayList<String>();

		for (String subscription : subscriptions) {
			if(rewardsMap.containsKey(subscription)){
				rewards.add(rewardsMap.get(subscription));
			}
		}

		return rewards;
	}
}
