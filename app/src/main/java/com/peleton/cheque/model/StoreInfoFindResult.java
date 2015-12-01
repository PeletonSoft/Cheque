package com.peleton.cheque.model;

public class StoreInfoFindResult {
	
	private boolean error;
	private String message;
	private boolean single;
	private boolean warning;
	private Integer storeInfoId;
	
	public boolean isError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public StoreInfoFindResult(boolean error, boolean single, 
			Boolean warning, String message, Integer storeInfoId) {
	
		this.message = message;
		this.error = error;
		this.single = single;
		this.warning = warning;
		this.storeInfoId =storeInfoId; 
		
	}

	public boolean isSingle() {
		return single;
	}

	public boolean isWarning() {
		return warning;
	}

	public Integer getStoreInfoId() {
		return storeInfoId;
	}

}
