package com.sky.reward.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sky.reward.controller.exceptions.ErrorResponse;
import com.sky.reward.controller.exceptions.InvalidAccountNumberException;
import com.sky.reward.controller.exceptions.TechnicalFailureException;
import com.sky.reward.controller.service.RewardService;

@RestController
public class RewardController {

	@Autowired
	private RewardService rewardService;

	@RequestMapping(value = "/rewards/{accountNumber}", method = RequestMethod.POST)
	public ResponseEntity<List<String>> fetchRewards(@PathVariable final String accountNumber,
													 @RequestBody final List<String> channels) throws InvalidAccountNumberException {

		List<String> rewards = new ArrayList<String>();
		try {
			rewards = rewardService.fetchRewards(accountNumber, channels);

			if (rewards == null || rewards.isEmpty()) {
				return new ResponseEntity<List<String>>(rewards, HttpStatus.NO_CONTENT);
			}

		} catch (InvalidAccountNumberException e) {
			throw new InvalidAccountNumberException("The supplied account number is invalid.");
		} catch (TechnicalFailureException e) {
			return new ResponseEntity<List<String>>(rewards, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<String>>(rewards, HttpStatus.OK);
	}

	@ExceptionHandler(InvalidAccountNumberException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}

}
