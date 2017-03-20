package com.sky.reward.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.sky.reward.RewardServiceApplication;
import com.sky.reward.controller.exceptions.InvalidAccountNumberException;
import com.sky.reward.controller.exceptions.TechnicalFailureException;
import com.sky.reward.controller.service.RewardService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardServiceApplication.class)
@WebAppConfiguration
public class RewardControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Mock
	private RewardService rewardServiceMock;

    @InjectMocks
    @Autowired
    private RewardController rewardController;


    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void requestMappingNotFound_WhenCustomerAccountNumber_IsNotSetInTheRequestPath() throws Exception {
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "").param("channels", "SPORTS", "NEWS")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isNotFound());
        
        verify(rewardServiceMock, times(0)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void badRequest_WhenNoChannelsAreSentInWithTheRequest() throws Exception {
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "1111").param("channels", "")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isBadRequest());
        
        verify(rewardServiceMock, times(0)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void returnRewards_ForEligibleCustomer_WithSubscriptionChannels_ThatHasRewardsAssociatedWithThem() throws Exception {
    	
    	List<String> rewards = new ArrayList<String>();
    	rewards.add("CHAMPIONS_LEAGUE_FINAL_TICKET");
    	rewards.add("PIRATES_OF_THE_CARIBBEAN_COLLECTION");
    	
    	when(rewardServiceMock.fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class))).thenReturn(rewards);
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "1111").param("channels", "SPORTS", "NEWS", "MOVIES")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$",  hasSize(2)))
                .andExpect(jsonPath("$[0]", equalTo("CHAMPIONS_LEAGUE_FINAL_TICKET")))
        		.andExpect(jsonPath("$[1]", equalTo("PIRATES_OF_THE_CARIBBEAN_COLLECTION")));
        
        verify(rewardServiceMock, times(1)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void returnNoRewards_ForEligibleCustomer_WithSubscriptionChannels_ThatHasNoRewardsAssociatedWithThem() throws Exception {
    	//given
    	List<String> emptyRewardsList = new ArrayList<String>();
    	
    	//when
    	when(rewardServiceMock.fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class))).thenReturn(emptyRewardsList);
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "1111").param("channels", "KIDS", "NEWS")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(contentType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
        
        //then
        verify(rewardServiceMock, times(1)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void returnNoRewards_ForInEligibleCustomer() throws Exception {
    	//given
    	List<String> emptyRewardsList = new ArrayList<String>();
    	
    	//when
    	when(rewardServiceMock.fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class))).thenReturn(emptyRewardsList);
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "2222").param("channels", "SPORTS", "NEWS")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(contentType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
        
        //then
        verify(rewardServiceMock, times(1)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void returnNoRewadrsAndNotifyClientOfInvalidAccountNumber_ForInvalidCustomerAccountNumber() throws Exception {
    	//given
    	//when
    	when(rewardServiceMock.fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class))).thenThrow((new InvalidAccountNumberException("The supplied account number is invalid.")));
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "3333").param("channels", "SPORTS", "NEWS")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("404"))
        		.andExpect(jsonPath("$.message").value("The supplied account number is invalid."));
        
        //then
        verify(rewardServiceMock, times(1)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }
    
    @Test
    public void throwTechnicalFailureException_ForAnyTechnicalFailure() throws Exception {
    	//given
    	//when
    	when(rewardServiceMock.fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class))).thenThrow(TechnicalFailureException.class);
    	
        mockMvc.perform(get("/rewards/{accountNumber}", "4444").param("channels", "SPORTS", "NEWS")
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isServiceUnavailable());
        
        //then
        verify(rewardServiceMock, times(1)).fetchRewards(Matchers.any(String.class), Matchers.anyListOf(String.class));
        verifyNoMoreInteractions(rewardServiceMock);
    }


}

