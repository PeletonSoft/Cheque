package com.peleton.cheque.model.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.StoreArticle;
import com.peleton.cheque.model.StoreInfoFindResult;
import com.peleton.cheque.model.StoreInfoSelectResult;
import com.peleton.cheque.model.User;

public class HttpShop implements Shop {

	private HttpShopFactory factory;
	private JSONObject json;
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
	public List<User> getUsers() {

		ArrayList<User> userArray = new ArrayList<User>();
		try {
			JSONArray array = json.getJSONArray("Сотрудники");

			for(int i = 0; i < array.length(); i++) {

				JSONObject json = array.getJSONObject(i);
				userArray.add(new HttpUser(factory, json));
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return userArray;
	}

	public HttpShop(HttpShopFactory factory, JSONObject json) {

		this.factory = factory;
		this.json = json;

		try {

			this.id  = json.getInt("Код");
			this.name  = json.getString("Название");

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
		if (o instanceof HttpShop) {
			return getId() == ((HttpShop)o).getId();
		}

		return false;

	}

	@Override
	public StoreInfoFindResult getStoreInfo(int articleId) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("articleId",
				String.valueOf(articleId)));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/InfoByArticleId.ashx"),params);

		try {
			JSONObject json = new JSONObject(text);

			Integer storeInfoId = null;
			try {

				storeInfoId = json.getInt("storeInfoId");
				if(storeInfoId == 0) {

					storeInfoId = null;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new StoreInfoFindResult(
					json.getBoolean("error"),
					json.getInt("posCount") == 1,
					json.getBoolean("warning"),
					json.getString("message"),
					storeInfoId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
            int ArticleId = Convert.ToInt32(context.Request["articleId"]);

            var JObject = new JObject(
                new JProperty("articleId", ArticleId),
                new JProperty("errorId", ErrorId),
                new JProperty("error", Error),
                new JProperty("message", Message),
                new JProperty("warning", Warning),
                new JProperty("posCount", PosCount),
                new JProperty("storeInfoId", StoreInfoId));

		 */

		return null;
	}

	@Override
	public StoreInfoSelectResult selectStoreInfo(int articleId) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("articleId",
				String.valueOf(articleId)));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/ContentSelect.ashx"),params);
		try {
			return new HttpStoreInfoSelectResult(factory, new JSONObject(text));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<StoreArticle> searchByArticle(String filter) {

		List<StoreArticle> list = new ArrayList<StoreArticle>();

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("filter",filter));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/SearchArticle/Content.ashx"),params);

		try {

			JSONObject json = new JSONObject(text);
			JSONArray array = json.getJSONArray("Список");

			for(int i = 0; i < array.length(); i++ ) {

				list.add(new HttpStoreArticle(factory, array.getJSONObject(i)));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return list;
	}

	@Override
	public void revise(int articleId, int price) {
		// TODO Auto-generated method stub

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("articleId",
				String.valueOf(articleId)));
		params.add(new BasicNameValuePair("price",
				String.valueOf(price)));
		factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/Revise.ashx"),params);
	}

}
