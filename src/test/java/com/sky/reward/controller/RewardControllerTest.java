package com.sky.reward.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.reward.RewardServiceApplication;
import com.sky.reward.controller.service.RewardService;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.*;

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
	private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;


    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	this.mockMvc = webAppContextSetup(webApplicationContext).build();
//        this.mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
    }

    @Test
    public void noRewardsFound() throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
    	List<String> channels = Arrays.asList("SPORTS", "NEWS");

    	//Object to JSON in String
    	String channelsJsonString = mapper.writeValueAsString(channels);
    	
    	String accountNumber = "2222";
    	List<String> subscriptions = Arrays.asList("SPORTS", "NEWS");
    	List<String> rewards = new ArrayList<String>();
    	rewards.add("CHAMPIONS_LEAGUE_FINAL_TICKET");
    	
//    	 RewardService rmock = Mockito.mock(RewardService.class);
//    	 when(rmock.fetchRewards(accountNumber, subscriptions)).thenReturn(rewards);
		when(rewardService.fetchRewards(accountNumber, subscriptions)).thenReturn(rewards);
//    	when(rewardService.fetchRewards(any(String.class), any(List.class))).thenReturn(rewards);
    	
        mockMvc.perform(post("/rewards/2222")
                .content(channelsJsonString)
                .contentType(contentType))
        		.andDo(print())
                .andExpect(status().isNoContent());
    }

}
