package com.peleton.cheque;

import java.util.List;

import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.StoreInfo;
import com.peleton.cheque.model.StoreInfoSelectResult;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

public class SelectActivity extends Activity {

	private int shopId;
	private int userId;
	private String password;
	private int articleId;
	private TextView articleIdView;
	private TextView articleView;
	private ListView positionsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);

		shopId = getIntent().getIntExtra("shopId", 3);
		userId = getIntent().getIntExtra("userId", 1);
		password = getIntent().getStringExtra("password");
		articleId = getIntent().getIntExtra("articleId", 1);

		articleIdView  = (TextView)findViewById(R.id.article_id);
		articleView  = (TextView)findViewById(R.id.article);
		positionsView = (ListView)findViewById(R.id.positions);
	}

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		AsyncTask<Void, Void, Void> resumeTask = new AsyncTask<Void, Void, Void>() {

			StoreInfoSelectResult storeInfoSelectResult;
			SelectStoreInfoAdapter adapter;
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Shop shop = getShopFactory().createShop(shopId);

				storeInfoSelectResult = shop.selectStoreInfo(articleId);

				List<StoreInfo> storeInfoList = storeInfoSelectResult.getStoreInfoList();
				StoreInfo[] storeInfos = storeInfoList.toArray(new StoreInfo[0]);

				adapter = new SelectStoreInfoAdapter
						(SelectActivity.this, R.layout.select_view, storeInfos);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				articleIdView.setText("Код: " +
						String.valueOf(storeInfoSelectResult.getArticleId()));

				articleView.setText(storeInfoSelectResult.getArticle());

				positionsView.setAdapter(adapter);
				super.onPostExecute(result);
			}

		};

		resumeTask.execute();
	}

}
