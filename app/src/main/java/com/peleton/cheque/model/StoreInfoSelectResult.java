package com.peleton.cheque.model;

import java.util.List;

public interface StoreInfoSelectResult {

	public int getArticleId();
	public String getArticle();
	public List<StoreInfo> getStoreInfoList();
}
