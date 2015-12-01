package com.peleton.cheque.model;

public class Authentication {

	private int userId;
	private int shopId;
	private String password;

	public int getShopId() {
	
		return this.shopId;
	}
	
	public int getUserId() {
		
		return this.userId;
	}
	
	public String getPassword() {
		
		return this.password;
	}
	
	public Authentication(int shopId, int userId, String password) {
		// TODO Auto-generated constructor stub
		this.shopId = shopId;
		this.userId = userId;
		this.password = password;
	}
}
