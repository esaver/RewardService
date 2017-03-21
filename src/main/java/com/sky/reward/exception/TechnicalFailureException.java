package com.sky.reward.exception;

public class TechnicalFailureException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public TechnicalFailureException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public TechnicalFailureException() {
		super();
	}
}
