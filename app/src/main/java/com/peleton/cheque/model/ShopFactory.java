package com.peleton.cheque.model;

import java.util.List;

public interface ShopFactory {

	public List<Shop> getShops();
	
	public Shop createShop(int id);
	
	public User createUser(int id);
	
	public void reset(); 
	
	public Cheque getLastCheque(Shop shop, User user);
	
}

