package com.peleton.cheque.model.http;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositionNotify;
import com.peleton.cheque.model.ChequePositions;

public class HttpChequePositions implements ChequePositions {

	private HttpShopFactory factory;
	private ArrayList<ChequePosition> innerChequePositions;
	private Cheque cheque;
	private ChequePositionNotify notify;

	@Override
	public Iterator<ChequePosition> iterator() {
		// TODO Auto-generated method stub
		return innerChequePositions.iterator();
	}

	@Override
	public void delete(ChequePosition chequePosition) {
		// TODO Auto-generated method stub
		try {


			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("positionId",
					String.valueOf(chequePosition.getId())));

			factory.getHttpToString(
					factory.getAuthHttpClient(),
					factory.createUrl("pages/Receipt/ContentDelete.ashx"),params);

			innerChequePositions.remove(chequePosition);
			if (getLength() == 0) {

				notify.onDeleteLast();
			}



		} catch(Exception e) {

		}
	}

	@Override
	public void add(int storeInfoId) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("storeInfoId",
				String.valueOf(storeInfoId)));

		String text = factory.getHttpToString(
				factory.getAuthHttpClient(),
				factory.createUrl("pages/Receipt/ContentInsert.ashx"),params);

		try {

			final JSONObject json = new JSONObject(text);
			int receiptNumber = json.getInt("receiptNumber");
			
			/*
			id = json.getInt("ID");
			articleId = json.getInt("Код_артикула");
			quant = json.getDouble("Количество");
			discount = json.getDouble("Скидка");
			price = json.getDouble("Цена_со_скидкой");
			 */

			JSONObject jsonPosition = new JSONObject() {{
				put("ID",json.getInt("positionId"));
				put("Код_артикула",json.getInt("articleId"));
				put("Количество",json.getDouble("quant"));
				put("Скидка",json.getDouble("discount"));
				put("Цена_со_скидкой",json.getDouble("price"));
			}};


			innerChequePositions.add(new HttpChequePosition(factory, jsonPosition));

			if(cheque.getNumber() == null) {
				notify.onAddFirst(receiptNumber);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
        int StoreInfoId = Convert.ToInt32(context.Request["storeInfoId"]);

        var JObject = new JObject(
            new JProperty("positionId", PositionId),
            new JProperty("quant", Quant),
            new JProperty("discount", Discount),
            new JProperty("price", Price),
            new JProperty("articleId", ArticleId),
            new JProperty("storeInfoId", StoreInfoId),
            new JProperty("receiptNumber", ReceiptNumber));
		 */

	}

	public int getLength() {
		ArrayList<ChequePosition> list = new ArrayList<ChequePosition>();
		for (ChequePosition chequePosition : this) {

			list.add(chequePosition);
		}

		return list.size();
	}

	@Override
	public ChequePosition[] toArray() {

		ChequePosition[] array = new ChequePosition[getLength()];

		int i=0;
		for(ChequePosition chequePosition : this){
			array[i++] = chequePosition;
		}

		return array;
	}

	public HttpChequePositions(HttpShopFactory factory,
							   Cheque cheque, ChequePositionNotify notify,
							   JSONArray jsonArray) {
		// TODO Auto-generated constructor stub

		this.factory = factory;
		this.cheque = cheque;
		this.notify = notify;

		innerChequePositions = new ArrayList<ChequePosition>();

		try {
			for(int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = jsonArray.getJSONObject(i);
				innerChequePositions.add(new HttpChequePosition(factory, json));
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}
}
