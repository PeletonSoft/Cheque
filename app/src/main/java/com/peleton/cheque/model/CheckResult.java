package com.peleton.cheque.model;

public class CheckResult {
	
	private boolean error;
	
	private String message;

	public boolean hasError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public CheckResult(boolean hasError, String message) {
		
		this.error = hasError;
		this.message = message;
	}
}
