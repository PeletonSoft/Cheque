package com.peleton.cheque.model;

import com.peleton.cheque.model.ArticleCharacteristic;

import java.util.List;

public interface ChequePositionInfo {

	public int getId();
	public int getArticleId();
	public String getArticle();
	public String getDescription();
	public String getProducer();
	public String getSupplier();	
	public String getType();
	public String getStore();
	public double getStoreQuant();
	public String getState();
	public String getGroup();
	public String getStorePlace();
	public String getAttrInfo();
	public String getAttr();
	public List<ArticleCharacteristic> getCharacteristics();
}
