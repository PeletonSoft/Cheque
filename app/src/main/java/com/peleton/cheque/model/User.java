package com.peleton.cheque.model;

public interface User {

	public int getId();
	
	public String getName();
	
	public CheckResult checkPassword(Shop shop, String password);
	
}
