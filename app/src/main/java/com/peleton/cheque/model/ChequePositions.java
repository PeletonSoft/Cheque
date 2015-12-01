package com.peleton.cheque.model;

public interface ChequePositions extends Iterable<ChequePosition> {
	
	public void delete(ChequePosition chequePosition); 
	public void add(int storeInfoId);
	public ChequePosition[] toArray();
}
