package com.peleton.cheque.model.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.StoreInfo;
import com.peleton.cheque.model.StoreInfoSelectResult;

public class HttpStoreInfoSelectResult implements StoreInfoSelectResult {

	int articleId;
	HttpShopFactory factory;
	ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();

	@Override
	public int getArticleId() {
		// TODO Auto-generated method stub
		return this.articleId;
	}

	@Override
	public String getArticle() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<StoreInfo> getStoreInfoList() {
		// TODO Auto-generated method stub
		return list;
	}

	public HttpStoreInfoSelectResult(HttpShopFactory factory, JSONObject json) {

		this.factory = factory;

		try {
			this.articleId = json.getInt("Код_артикула");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray array;
		try {

			array = json.getJSONArray("Позиции");
			for(int i = 0; i < array.length(); i++) {

				list.add(new HttpStoreInfo(factory, array.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
