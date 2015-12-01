package com.peleton.cheque.model.http;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositionInfo;

public class HttpChequePosition implements ChequePosition {

	private HttpShopFactory factory;
	private int id;
	private int articleId;
	private double quant;
	private double discount;
	private double price;

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int getArticleId() {
		// TODO Auto-generated method stub
		return articleId;
	}

	@Override
	public double getQuant() {
		// TODO Auto-generated method stub
		return quant;
	}

	@Override
	public double getDiscount() {
		// TODO Auto-generated method stub
		return discount;
	}

	@Override
	public double getPrice() {
		// TODO Auto-generated method stub
		return price;
	}

	@Override
	public void setQuant(double quant) {

		if(quant < 0.01f) {

			quant = 0.01f;
		}
		this.quant = Math.round(quant * 100.0f) *0.01f;
		refresh();

	}

	@Override
	public void setDiscount(double discount) {

		this.discount = Math.round(discount * 100.0f) *0.01f;
		refresh();
	}

	public HttpChequePosition(HttpShopFactory factory, JSONObject json) {
		// TODO Auto-generated constructor stub
		this.factory = factory;
		/*
      	"ID": 1473396,
      	"Код_размещения": 3,
      	"Код_артикула": 2,
      	"Количество": 1.0,
      	"Скидка": 0.0,
      	"Цена_со_скидкой": 145.0
		*/
		try {

			id = json.getInt("ID");
			articleId = json.getInt("Код_артикула");
			quant = json.getDouble("Количество");
			discount = json.getDouble("Скидка");
			price = json.getDouble("Цена_со_скидкой");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refresh() {
		// TODO Auto-generated method stub
		/*
  			"positionId": 1473396,
  			"quant": 2.0,
  			"discount": 0.0,
  			"price": 145.0
		 */
		//positionId=1473396&quant=2&discount=0

		String quantText = String.valueOf(getQuant()).replace(',', '.');
		String discountText = String.valueOf(getDiscount()).replace(',', '.');

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("positionId", String.valueOf(getId())));
		params.add(new BasicNameValuePair("quant", quantText));
		params.add(new BasicNameValuePair("discount",discountText));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/ContentUpdate.ashx"),params);

		try {
			JSONObject resultJSON = new JSONObject(text);
			quant = resultJSON.getDouble("quant");
			discount = resultJSON.getDouble("discount");
			price = resultJSON.getDouble("price");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean equals(Object o) {
		return
				o != null &&
						o instanceof HttpChequePosition &&
						this.id == ((HttpChequePosition)o).getId();
	}

	@Override
	public int hashCode() {

		return Integer.valueOf(id).hashCode();
	}

	@Override
	public ChequePositionInfo getChequePositionInfo() {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("positionId", String.valueOf(getId())));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/InfoByPositionId.ashx"),params);

		try {
			return new HttpChequePositionInfo(factory, new JSONObject(text));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

