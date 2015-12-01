package com.peleton.cheque.model.fake;

import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositions;
import com.peleton.cheque.model.SendResult;
import com.peleton.cheque.model.Shop;

public class FakeCheque implements Cheque {

	private int number;
	private Shop shop;
	private ChequePositions chequePositions;
	
	public Integer getNumber() {
		
		return number;
	}
	
	public Shop getShop() {
		
		return shop;
	}
	
	public FakeCheque(Shop shop, int number) {

		this.shop = shop;
		this.number = number;
	}

	@Override
	public ChequePositions getChequePositions() {
		
		if(chequePositions == null) {
			
			chequePositions = new FakeChequePositions() {{ 
				add(5); 
				add(10); 
				add(11); 
			}};
		}
		return chequePositions;
	}

	@Override
	public double getSum() {
		// TODO Auto-generated method stub
		double sum = 0;
		for(ChequePosition chequePosition : getChequePositions()) {
			
			double quant = Math.round(chequePosition.getQuant() * 100.0f) * 0.01f;
			double price = Math.round(chequePosition.getPrice() * 100.0f) * 0.01f;
			double positionSum = Math.round(quant * price * 100.0f) * 0.01f;
			sum += positionSum;
		}
		return sum;
	}

	@Override
	public SendResult send() {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
