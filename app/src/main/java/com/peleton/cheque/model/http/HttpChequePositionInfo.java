package com.peleton.cheque.model.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.ArticleCharacteristic;
import com.peleton.cheque.model.ChequePositionInfo;

public class HttpChequePositionInfo implements ChequePositionInfo {

	private String store;
	private double storeQuant;
	private String type;
	private String producer;
	private String description;
	private String article;
	private int articleId;
	private int id;
	private String state;
	private String group;

	private String storePlace;

	private String attrInfo;

	private String attr;
	private String supplier;
	private List<ArticleCharacteristic> characteristics;

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

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
	public String getProducer() {
		// TODO Auto-generated method stub
		return this.producer;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public double getStoreQuant() {
		// TODO Auto-generated method stub
		return this.storeQuant;
	}

	public HttpChequePositionInfo(HttpShopFactory factory, JSONObject json) {

		/*
		 {
 			"ID": 1473411,
 			"Код_артикула": 2,
    		"Артикул": "0001",
    		"Описание": "Клей QUELYD спец-винил 300гр",
 			"ПрИнфо": "Штука",
 			"Инфо": "",
 			"Вид_товара": "Клей",
 			"Производитель": "Quelyd",
 			"Склад": "ОБ-ИЦ",
 			"Статус": "",
 			"Группа": "",
 			"Сектор": "0.1,19c,2,201a,З 6,КЛЕЙ",
 			"Цена": 145.0,
 			"Количество": 99.0

		 */
		try {

			this.id = json.getInt("ID");
			this.articleId = json.getInt("Код_артикула");
			this.article = json.getString("Артикул");
			this.description = json.getString("Описание");
			this.producer = json.getString("Производитель");
			this.type = json.getString("Вид_товара");
			this.store = json.getString("Склад");
			this.state = json.getString("Статус");
			this.group = json.getString("Группа");
			this.storePlace = json.getString("Сектор");
			this.storeQuant = json.getDouble("Количество");
			this.attrInfo = json.getString("Инфо");
			this.supplier = json.getString("Поставщик");
			this.attr = json.getString("ПрИнфо");

			JSONArray list = json.getJSONArray("Характеристики");
			characteristics = new ArrayList<ArticleCharacteristic>();
			for(int i = 0; i < list.length(); i++) {
				characteristics.add(new HttpArticleCharacteristic(factory, list.getJSONObject(i)));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getStore() {
		// TODO Auto-generated method stub
		return this.store;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return this.state;
	}

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return this.group;
	}

	@Override
	public String getStorePlace() {
		// TODO Auto-generated method stub
		return this.storePlace;
	}

	@Override
	public String getAttrInfo() {
		// TODO Auto-generated method stub
		return this.attrInfo;
	}

	@Override
	public String getAttr() {
		// TODO Auto-generated method stub
		return this.attr;
	}

	@Override
	public String getSupplier() {
		// TODO Auto-generated method stub
		return this.supplier;
	}

	@Override
	public List<ArticleCharacteristic> getCharacteristics() {
		// TODO Auto-generated method stub
		return characteristics;
	}
}
