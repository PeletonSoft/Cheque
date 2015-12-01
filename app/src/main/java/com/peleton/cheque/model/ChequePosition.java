package com.peleton.cheque.model;

public interface ChequePosition {

	public int getId();
	public int getArticleId();
	public double getQuant();
	public double getDiscount();
	public double getPrice();
	
	public void setQuant(double quant);
	public void setDiscount(double discount);
	
	public ChequePositionInfo getChequePositionInfo();
}
