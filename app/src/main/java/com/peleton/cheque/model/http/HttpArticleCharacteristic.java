package com.peleton.cheque.model.http;

import org.json.JSONException;
import org.json.JSONObject;


import com.peleton.cheque.model.ArticleCharacteristic;

public class HttpArticleCharacteristic implements ArticleCharacteristic {

	private String key;
	private String value;

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}

	public HttpArticleCharacteristic(HttpShopFactory factory, JSONObject json) {

		try {

			key = json.getString("Характеристика");
			value = json.getString("Значение");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getKey() + ": " + getValue();
	}
}
