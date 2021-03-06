package com.sky.reward.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sky.reward.exception.InvalidAccountNumberException;
import com.sky.reward.exception.TechnicalFailureException;
import com.sky.reward.service.EligibilityService;
import com.sky.reward.service.RewardService;

@RunWith(MockitoJUnitRunner.class)
public class RewardServiceImplTest {

	@Mock
	private EligibilityService eligibilityServiceMock;

	@InjectMocks
	private RewardService rewardService = new RewardServiceImpl();
	
	String accountNumber = "1111";
	List<String> subscriptionsWithRewards;
	List<String> subscriptionsWithOutRewards;
	
	@Before
	public void setUp(){
		subscriptionsWithRewards = Arrays.asList("SPORTS", "NEWS", "MOVIES");
		subscriptionsWithOutRewards = Arrays.asList("KIDS", "NEWS");
	}

	@Test
	public void whenCustomer_IsEligible_AndHisSubscriptions_HasRewardsAssociatedWithThem_ReturnRewards() throws Exception {
		
		when(eligibilityServiceMock.checkRewardEligibility(accountNumber)).thenReturn("CUSTOMER_ELIGIBLE");

		List<String> actualRewards = rewardService.fetchRewards(accountNumber, subscriptionsWithRewards);

		List<String> expectedRewards = Arrays.asList("CHAMPIONS_LEAGUE_FINAL_TICKET", "PIRATES_OF_THE_CARIBBEAN_COLLECTION");
		Assert.assertEquals(2, actualRewards.size());
		Assert.assertEquals(expectedRewards, actualRewards);
		Assert.assertEquals(expectedRewards.get(0), actualRewards.get(0));
		
		verify(eligibilityServiceMock, times(1)).checkRewardEligibility(Matchers.any(String.class));
        verifyNoMoreInteractions(eligibilityServiceMock);
	}

	@Test
	public void whenCustomer_IsEligible_AndHisSubscriptions_HasNoRewardsAssociatedWithThem_ReturnNoRewards() throws Exception {

		when(eligibilityServiceMock.checkRewardEligibility(accountNumber)).thenReturn("CUSTOMER_ELIGIBLE");

		List<String> actualRewards = rewardService.fetchRewards(accountNumber, subscriptionsWithOutRewards);
		List<String> expectedRewards = new ArrayList<>();

		Assert.assertEquals(0, actualRewards.size());
		Assert.assertEquals(expectedRewards, actualRewards);
		
		verify(eligibilityServiceMock, times(1)).checkRewardEligibility(Matchers.any(String.class));
        verifyNoMoreInteractions(eligibilityServiceMock);
	}

	@Test
	public void whenCustomer_IsNotEligible_ReturnNoRewards() throws Exception {

		when(eligibilityServiceMock.checkRewardEligibility(accountNumber)).thenReturn("CUSTOMER_INELIGIBLE");

		List<String> actualRewards = rewardService.fetchRewards(accountNumber, subscriptionsWithRewards);
		List<String> expectedRewards = new ArrayList<>();

		Assert.assertEquals(0, actualRewards.size());
		Assert.assertEquals(expectedRewards, actualRewards);
		
		verify(eligibilityServiceMock, times(1)).checkRewardEligibility(Matchers.any(String.class));
        verifyNoMoreInteractions(eligibilityServiceMock);
	}

	@Test
	public void whenInvalidCustomerAccountNumberIsPassed_ThrowInvalidAccountNumberExceptionWithErrorMessage() throws Exception {

		InvalidAccountNumberException exception = null;
		
		when(eligibilityServiceMock.checkRewardEligibility(accountNumber)).thenThrow(new InvalidAccountNumberException("The supplied account number is invalid."));

		try {
			rewardService.fetchRewards(accountNumber, subscriptionsWithRewards);
		} catch (InvalidAccountNumberException e) {
			exception = e;
		}

		Assert.assertNotNull(exception);
		Assert.assertEquals("The supplied account number is invalid.", exception.getErrorMessage());
		
		verify(eligibilityServiceMock, times(1)).checkRewardEligibility(Matchers.any(String.class));
        verifyNoMoreInteractions(eligibilityServiceMock);
	}
	
	@Test(expected = TechnicalFailureException.class)
	public final void whenEligibilityServiceIsDown_ThrowTechnicalFailureException() throws Exception {
		
		when(eligibilityServiceMock.checkRewardEligibility(accountNumber)).thenThrow(new TechnicalFailureException("Service technical failure"));
		rewardService.fetchRewards(accountNumber, subscriptionsWithRewards);
	}

}
