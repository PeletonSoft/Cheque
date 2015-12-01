package com.peleton.cheque.model.fake;

import com.peleton.cheque.model.StoreInfo;

public class FakeStoreInfo implements StoreInfo {

	private int id;
	private String attr;
	private String attrInfo;
	private String store;
	private double quant;

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getAttr() {
		// TODO Auto-generated method stub
		return attr;
	}

	@Override
	public String getAttrInfo() {
		// TODO Auto-generated method stub
		return attrInfo;
	}

	@Override
	public String getStore() {
		// TODO Auto-generated method stub
		return store;
	}

	@Override
	public double getQuant() {
		// TODO Auto-generated method stub
		return quant;
	}

	public FakeStoreInfo(int id, String attr, String attrInfo, String store, double quant) {
		
		this.id = id;
		this.attr = attr;
		this.attrInfo = attrInfo;
		this.store = store;
		this.quant = quant;
	}
}
