package com.peleton.cheque.model.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.CheckResult;
import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.User;

public class HttpShopFactory implements ShopFactory {

	private DefaultHttpClient httpClient;
	private Date lastHttpClient;
	private DefaultHttpClient authHttpClient;
	private JSONObject shopArrayJSON;
	private String server;
	private String key;
	private String theme;
	private int defaultShopId;
	private Authentication authentication;


	public HttpShopFactory(String server, String key, String theme,
						   int defaultShopId, Authentication authentication) {

		this.server = server;
		this.key = key;
		this.theme = theme;
		this.defaultShopId = defaultShopId;
		this.authentication = authentication;
	}

	public String createUrl(String text) {

		return "http://" + server + "/" + text;

	}

	@Override
	public List<Shop> getShops() {
		// TODO Auto-generated method stub

		if(shopArrayJSON == null) {
			String text = getHttpToString(getHttpClient(), createUrl("pages/Login/SelectShops.ashx"));
			try {
				shopArrayJSON = new JSONObject(text);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ArrayList<Shop> shopArray = new ArrayList<Shop>();
		try {
			JSONArray array = shopArrayJSON.getJSONArray("Магазины");

			for(int i = 0; i < array.length(); i++) {

				JSONObject json = array.getJSONObject(i);
				shopArray.add(new HttpShop(this, json));
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return shopArray;
	}

	@Override
	public Shop createShop(int id) {


		for(Shop shop : getShops()) {

			if (shop.getId() == id)
				return shop;
		}
		return null;
	}

	@Override
	public User createUser(int id) {
		// TODO Auto-generated method stub

		for(Shop shop : getShops()) {

			for(User user : shop.getUsers()) {
				if (user.getId() == id)
					return user;
			}
		}

		return null;
	}

	@Override
	public Cheque getLastCheque(Shop shop, User user) {

		try {

			String text = getHttpToString(
					getAuthHttpClient(),
					createUrl("pages/Receipt/Content.ashx"));

			return new HttpCheque(this,new JSONObject(text));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	public DefaultHttpClient getHttpClient() {

		if(httpClient != null) {

			Date current = Calendar.getInstance().getTime();
			long diffInMillies = current.getTime() - lastHttpClient.getTime();
			long diffSeconds = TimeUnit.SECONDS.convert
					(diffInMillies,TimeUnit.MILLISECONDS);

			if(diffSeconds > 20) {

				httpClient = null;
			}
		}

		if(httpClient == null) {

			lastHttpClient = Calendar.getInstance().getTime();
			httpClient = new DefaultHttpClient();

			String url = createUrl("pages/default/main.aspx") + "?" +
					"key=" + key +
					"&" + "theme=" + theme +
					"&" + "shopId=" + String.valueOf(defaultShopId);

			getHttpToString(httpClient, url);
		}

		return httpClient;
	}

	public DefaultHttpClient getAuthHttpClient() {

		DefaultHttpClient httpClient = getHttpClient();

		if (this.authHttpClient == httpClient) {

			return this.authHttpClient;
		}

		if (authentication == null) {

			return httpClient;
		}

		CheckResult result = checkPassword(this.authentication);

		if(!result.hasError()) {

			this.authHttpClient = httpClient;
		}

		return this.authHttpClient;


	}

	public String getHttpToString(DefaultHttpClient httpClient, final String url) {

		return  getHttpToString(httpClient, url, new ArrayList<NameValuePair>());

	}

	public String getHttpToString(DefaultHttpClient httpClient, final String url, List<NameValuePair> post) {

		String result = "";

		try {

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(post));
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();



			if (entity != null) {

				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				instream.close();
			}

		} catch (Exception e) {

		}

		return result;
	}

	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public CheckResult checkPassword(Authentication authentication) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ShopId", String.valueOf(authentication.getShopId())));
		params.add(new BasicNameValuePair("UserId", String.valueOf(authentication.getUserId())));
		params.add(new BasicNameValuePair("Password", authentication.getPassword()));

		String text = getHttpToString(
				getHttpClient(),
				createUrl("pages/Login/Authentication.ashx"),params);

		JSONObject checkResultJSON;
		try {
			checkResultJSON = new JSONObject(text);
			boolean hasSuccess = checkResultJSON.getBoolean("hasSuccess");

			if(hasSuccess) {

				this.authentication = authentication;
			}

			return new CheckResult(
					!hasSuccess,
					checkResultJSON.getString("message"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CheckResult(true, "Ошибка на сервере.");

	}

	@Override
	public void reset() {

		httpClient = null;
	}
}
