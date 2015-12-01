package com.peleton.cheque;

import com.peleton.cheque.model.ArticleCharacteristic;
import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositionInfo;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

public class InfoActivity extends Activity {

	int userId;
	int shopId;
	private String password;

	int positionId;

	private ShopFactory shopFactory;

	private ShopFactory getShopFactory() {

		if(shopFactory == null) {

			Authentication authentication =
					new Authentication(shopId, userId, password);

			ShopFactoryService service =
					new ShopFactoryService(this);
			shopFactory = service.createShopFactory(authentication);

		}
		return shopFactory;
	}

	private TextView articleIdView;
	private TextView articleView;
	private TextView descriptionView;
	private TextView producerView;
	private TextView supplierView;
	private TextView typeView;
	private TextView storeView;
	private TextView stateView;
	private TextView groupView;
	private TextView storePlaceView;
	private TextView storeQuantView;
	private TextView attrInfoView;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		shopId = getIntent().getIntExtra("shopId", 3);
		userId = getIntent().getIntExtra("userId", 1);
		password = getIntent().getStringExtra("password");
		positionId = getIntent().getIntExtra("positionId", 0);

		articleIdView = (TextView)findViewById(R.id.article_id);
		articleView = (TextView)findViewById(R.id.article);
		descriptionView = (TextView)findViewById(R.id.description);
		producerView = (TextView)findViewById(R.id.producer);
		supplierView = (TextView)findViewById(R.id.supplier);
		typeView = (TextView)findViewById(R.id.type);
		storeView = (TextView)findViewById(R.id.store);
		stateView = (TextView)findViewById(R.id.state);
		groupView = (TextView)findViewById(R.id.group);
		storePlaceView = (TextView)findViewById(R.id.store_place);
		storeQuantView = (TextView)findViewById(R.id.store_quant);
		attrInfoView = (TextView)findViewById(R.id.attr_info);

		listView = (ListView)findViewById(R.id.list);
	}

	@Override
	protected void onResume() {

		AsyncTask<Void, Void, Void> viewTask = new AsyncTask<Void, Void, Void>() {

			ChequePositionInfo chequePositionInfo;
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Shop shop = getShopFactory().createShop(shopId);
				User user = getShopFactory().createUser(userId);
				Cheque cheque = getShopFactory().getLastCheque(shop, user);

				ChequePosition chequePosition = null;
				for(ChequePosition position : cheque.getChequePositions()) {

					if (position.getId() == positionId) {
						chequePosition = position;
						break;
					}
				}

				if (chequePosition == null) {

					return null;
				}
				chequePositionInfo = chequePosition.getChequePositionInfo();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {

				if(chequePositionInfo != null) {

					articleIdView.setText("Код: " + String.valueOf(chequePositionInfo.getArticleId()));
					articleView.setText("Артикул: " + chequePositionInfo.getArticle());
					descriptionView.setText("Описание: " + chequePositionInfo.getDescription());
					typeView.setText("Вид товара: " + chequePositionInfo.getType());
					producerView.setText("Производитель: " + chequePositionInfo.getProducer());
					supplierView.setText("Поставщик: " + chequePositionInfo.getSupplier());
					storeView.setText("Склад: " + chequePositionInfo.getStore());
					stateView.setText("Статус: " + chequePositionInfo.getState());
					groupView.setText("Группа: " + chequePositionInfo.getGroup());
					storePlaceView.setText("Сектор: " + chequePositionInfo.getStorePlace());
					attrInfoView.setText(chequePositionInfo.getAttr() + ": " +
							chequePositionInfo.getAttrInfo());
					storeQuantView.setText("На складе: " +
							String.format("%1.2f", chequePositionInfo.getStoreQuant()));

					ArrayAdapter<ArticleCharacteristic> adapter = new ArrayAdapter<ArticleCharacteristic>(
							InfoActivity.this,
							android.R.layout.simple_list_item_1,
							chequePositionInfo.getCharacteristics());
					listView.setAdapter(adapter);
				}

				super.onPostExecute(result);
			}
		};

		viewTask.execute();
		super.onResume();

	}

}
