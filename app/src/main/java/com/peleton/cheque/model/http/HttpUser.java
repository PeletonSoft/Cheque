package com.peleton.cheque.model.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.CheckResult;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.User;

public class HttpUser implements User {

	private HttpShopFactory factory;
	private int id;
	private String name;

	@Override
	public int getId() {

		return id;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public CheckResult checkPassword(Shop shop, String password) {

		Authentication authentication = new Authentication
				(shop.getId(), getId(), password);

		return factory.checkPassword(authentication);
	}

	public HttpUser(HttpShopFactory factory, JSONObject json) {

		this.factory = factory;
		try {

			this.id  = json.getInt("Код");
			this.name  = json.getString("ФИО");

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return ((Integer)getId()).hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof HttpUser) {
			return getId() == ((HttpShop)o).getId();
		}

		return false;

	}

}
