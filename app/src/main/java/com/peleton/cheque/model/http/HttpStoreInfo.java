package com.peleton.cheque.model.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.StoreInfo;

public class HttpStoreInfo implements StoreInfo {

	private int id;
	private String attr;
	private String attrInfo;
	private String store;
	private double quant;
	//private HttpShopFactory factory;

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

	public HttpStoreInfo(HttpShopFactory factory, JSONObject json) {
		/*
		       "Код_размещения": 3,
      "ПрИнфо": "Штука",
      "Инфо": "",
      "Аббревиатура": "ОБ-ИЦ",
      "Количество": 99.0
		 */

		try {

			this.id = json.getInt("Код_размещения");
			this.quant = json.getDouble("Количество");
			this.attr = json.getString("ПрИнфо");
			this.attrInfo = json.getString("Инфо");
			this.store = json.getString("Аббревиатура");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
