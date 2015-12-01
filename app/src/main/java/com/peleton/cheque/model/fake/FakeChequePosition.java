package com.peleton.cheque.model.fake;

import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositionInfo;

public class FakeChequePosition implements ChequePosition {

	public FakeChequePosition(int id) {

		this.id = id;
	}

	private int id;
	private Integer articleId;
	private Double quant;
	private Double discount;
	private Double price;
	
	public void refresh() {
		
		articleId = id;
		quant = 5.0;
		discount = 5.0;
		price = 1200.0;
	}
	
	
	@Override
	public int getArticleId() {
		// TODO Auto-generated method stub
		if(articleId == null) {
			
			refresh();
		}
			
		return articleId;
	}

	@Override
	public double getQuant() {
		// TODO Auto-generated method stub
		if(quant == null) {
			
			refresh();
		}
		
		return quant;
	}

	@Override
	public double getDiscount() {
		// TODO Auto-generated method stub
		if(discount == null) {
			
			refresh();
		}
		
		return discount;
	}

	@Override
	public double getPrice() {
		// TODO Auto-generated method stub
		if(price == null) {
			
			refresh();
		}
		
		return price;
	}

	@Override
	public void setQuant(double quant) {
		
		if (quant < 0.01f) {
			
			quant = 0.01f;
		}
		
		this.quant = quant;			
	}

	@Override
	public void setDiscount(double discount) {
		
		if (discount < 0.00f) {
			
			discount = 0.00f;
		}
		
		if (discount > 100.00f) {
			
			discount = 100.00f;
		}
		
		this.discount = discount;
		
	}


	@Override
	public int getId() {

		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return  
				o != null && 
				o instanceof FakeChequePosition && 
				this.id == ((FakeChequePosition)o).id;
	}
	
	@Override
	public int hashCode() {

		return Integer.valueOf(id).hashCode();
	}


	@Override
	public ChequePositionInfo getChequePositionInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
