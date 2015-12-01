package com.peleton.cheque.model;

public class SendResult {
	
	private boolean error;
	private String message;

	public boolean isError() {
		return this.error;
	}
	
	public String getMessage() {
		
		return this.message;
	}
	
	public SendResult(boolean error, String message ) {
		
		this.message = message;
		this.error = error;
	}
}
