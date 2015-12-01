package com.peleton.cheque.model;

import java.util.List;

public interface Shop {

	public int getId();
	
	public String getName();
	
	public List<User> getUsers();
	
	public StoreInfoFindResult getStoreInfo(int articleId);
	
	public StoreInfoSelectResult selectStoreInfo(int articleId);
	
	public List<StoreArticle> searchByArticle(String filter);

	public void revise(int articleId, int price);
	
}
