package com.peleton.cheque.model.http;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositionNotify;
import com.peleton.cheque.model.ChequePositions;
import com.peleton.cheque.model.SendResult;

public class HttpCheque implements Cheque {

	private HttpShopFactory factory;
	private JSONObject json;
	private ChequePositions chequePositions;
	private Integer number;

	@Override
	public ChequePositions getChequePositions() {
		// TODO Auto-generated method stub
		if(chequePositions == null) {

			JSONArray jsonPositions;
			try {
				jsonPositions = json.getJSONArray("Чек");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				jsonPositions = new JSONArray();
			}

			ChequePositionNotify notify = new ChequePositionNotify() {

				@Override
				public void onDeleteLast() {

					number = null;
				}

				@Override
				public void onAddFirst(int receiptNumber) {

					number = receiptNumber;
				}
			};

			chequePositions = new HttpChequePositions(factory, this, notify, jsonPositions);
		}
		return chequePositions;
	}

	public HttpCheque(HttpShopFactory factory, JSONObject json) {
		// TODO Auto-generated constructor stub

		this.factory = factory;
		this.json = json;

		try {
			this.number = json.getInt("Номер_чека");

			if (this.number == 0) {

				number = null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			this.number = null;
		}


	}

	@Override
	public Integer getNumber() {
		// TODO Auto-generated method stub
		return number;
	}

	@Override
	public double getSum() {
		// TODO Auto-generated method stub
		double sum = 0;
		for(ChequePosition chequePosition : getChequePositions()) {

			double quant = Math.round(chequePosition.getQuant() * 100.0f) * 0.01f;
			double price = Math.round(chequePosition.getPrice() * 100.0f) * 0.01f;
			double positionSum = Math.round(quant * price * 100.0f) * 0.01f;
			sum += positionSum;
		}
		return sum;
	}

	@Override
	public SendResult send() {

		if(number != null) {

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("UserId", "0"));

			String text = factory.getHttpToString(
					factory.getAuthHttpClient(),
					factory.createUrl("pages/Receipt/ContentSend.ashx"),params);

			JSONObject json;
			try {
				json = new JSONObject(text);

				boolean error = json.getBoolean("error");
				String message = error ? json.getString("message") :
						"Чек №" + number.toString() + " был успешно отправлен";
				return new SendResult(error, message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return new SendResult(true, "Ошибка при отправке чека");
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

		if(number != null) {

			factory.getHttpToString(
					factory.getAuthHttpClient(),
					factory.createUrl("pages/Receipt/ContentClear.ashx"));
		}
	}

}
