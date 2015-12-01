package com.peleton.cheque.model;

public interface Cheque {
	
	public ChequePositions getChequePositions();
	
	public Integer getNumber();
	
	public double getSum();
	
	public SendResult send();
	
	public void clear();
}
