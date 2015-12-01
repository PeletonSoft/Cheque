package com.peleton.cheque.model.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.StoreArticle;

public class HttpStoreArticle implements StoreArticle {

	private int articleId;
	private String article;
	private String description;
	private String store;
	private double quant;

	@Override
	public int getArticleId() {
		// TODO Auto-generated method stub
		return this.articleId;
	}

	@Override
	public String getArticle() {
		// TODO Auto-generated method stub
		return this.article;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}

	@Override
	public String getStore() {
		// TODO Auto-generated method stub
		return this.store;
	}

	@Override
	public double getQuant() {
		// TODO Auto-generated method stub
		return this.quant;
	}


	public HttpStoreArticle(HttpShopFactory factory, JSONObject json) {
		// TODO Auto-generated constructor stub
		/*
		new JProperty("Код_артикула", rw.Код_артикула),
        new JProperty("Количество", rw.Количество),
        new JProperty("Описание", rw.Описание),
        new JProperty("Артикул", rw.Артикул),
        new JProperty("Склад", rw.Склад)
		*/
		try {
			this.articleId = json.getInt("Код_артикула");
			this.article = json.getString("Артикул");
			this.description = json.getString("Описание");
			this.store = json.getString("Склад");
			this.quant = json.getDouble("Количество");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
