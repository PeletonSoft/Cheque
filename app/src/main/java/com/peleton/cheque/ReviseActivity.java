package com.peleton.cheque;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReviseActivity extends Activity {

	private int shopId;
	private int userId;
	private String password;

	TextView articleIdView;
	TextView priceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revise);

		shopId = getIntent().getIntExtra("shopId", 3);
		userId = getIntent().getIntExtra("userId", 1);
		password = getIntent().getStringExtra("password");

		articleIdView = (TextView)findViewById(R.id.article_id);
		priceView = (TextView)findViewById(R.id.price);

		ImageButton scanView = (ImageButton)findViewById(R.id.scan);
		scanView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startScan();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.revise, menu);
		return true;
	}

	private void startScan() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(ReviseActivity.this);
		scanIntegrator.initiateScan();
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

	private Shop getShop() {

		int shopId = getIntent().getIntExtra("shopId", 0);
		return getShopFactory().createShop(shopId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == IntentIntegrator.REQUEST_CODE) {


			IntentResult scanningResult =
					IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if(scanningResult != null) {

				String scanContent = scanningResult.getContents();


				if(scanContent != null && scanContent.length() >= 10) {

					try {
						final int articleId = Integer.parseInt(scanContent.substring(0, 5));
						final int price = Integer.parseInt(scanContent.substring(5, 10));

						checkPrice(articleId, price);

					}
					catch (Exception e) {

						articleIdView.setText(e.toString());
					}

				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void checkPrice(final int articleId, final int price) {

		articleIdView.setText("Код: " + String.valueOf(articleId));
		priceView.setText("Цена: " + String.valueOf(price));

		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//Shop shop = getShop();
				try {
					getShop().revise(articleId, price);
				}
				catch(Exception e) {
				}
				return null;
			}
		};
		task.execute();
		startScan();

	}
}

