package com.sky.reward.controller.exceptions;

public class InvalidAccountNumberException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public InvalidAccountNumberException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public InvalidAccountNumberException() {
		super();
	}
}
