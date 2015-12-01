package com.peleton.cheque;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.peleton.cheque.model.CheckResult;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity {

	Spinner shopListView;
	Spinner userListView;
	TextView errorView;
	EditText passwordInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		selectedShopId = getDefaultShopId();
		shopListView = (Spinner)findViewById(R.id.shop_list);
		userListView = (Spinner)findViewById(R.id.user_list);

		errorView = (TextView)findViewById(R.id.error);
		passwordInput = (EditText)findViewById(R.id.password);

		passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub

				checkPasswordAsync();
				return true;
			}
		});
	}


	protected void checkPasswordAsync() {
		// TODO Auto-generated method stub

		errorView.setText("");

		if (shopListView.getCount() == 0) {

			errorView.setText("Вы не выбрали магазин.");
			return;
		}

		if (userListView.getCount() == 0) {

			errorView.setText("Вы не выбрали сотрудника.");
			return;
		}

		final Shop selectedShop =  (Shop)shopListView.getSelectedItem();
		final User selectedUser =  (User)userListView.getSelectedItem();
		final String password = passwordInput.getText().toString();

		AsyncTask<Void, Void, Void> checkPaswordTask =
				new AsyncTask<Void, Void, Void>() {

					CheckResult checkPasswordResult;

					@Override
					protected Void doInBackground(Void... params) {


						checkPasswordResult = selectedUser.checkPassword(selectedShop, password);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						super.onPostExecute(result);
						errorView.setText(checkPasswordResult.getMessage());

						if (!checkPasswordResult.hasError()) {

							Intent intent = new Intent();
							intent.putExtra("shopId", selectedShop.getId());
							intent.putExtra("userId", selectedUser.getId());
							intent.putExtra("password", password);
							setResult(RESULT_OK, intent);
							finish();
						}
						else {

							passwordInput.setText("");
							return;
						}
					}
				};

		checkPaswordTask.execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setShopViewAsync();

		viewKeyboard();

	}


	private void viewKeyboard() {
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(passwordInput, InputMethodManager.SHOW_IMPLICIT);
			}
		};

		final Timer timer = new Timer();
		timer.schedule(tt, 200);
	}

	private ShopFactory shopFactory;

	private ShopFactory getShopFactory() {

		if(shopFactory == null) {

			ShopFactoryService service =
					new ShopFactoryService(this);
			shopFactory = service.createShopFactory(null);
		}
		return shopFactory;
	}


	private int getDefaultShopId() {

		int defaultShopId = 0;

		try {

			SharedPreferences settings =
					PreferenceManager.getDefaultSharedPreferences(getBaseContext());

			defaultShopId = settings.getInt(
					getResources().getString(R.string.preferences_shop), 0);

		} catch(Exception e) { }

		//return new FakeShopFactory();

		return defaultShopId;
	}

	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		FixCurrentState();

		if (selectedShopId != null) {

			outState.putInt("SelectedShop",  selectedShopId);
		}

		if (selectedUserId != null) {

			outState.putInt("SelectedUser",  selectedUserId);
		}
	}

	Integer selectedShopId;
	Integer selectedUserId;

	private void FixCurrentState() {
		if (shopListView.getCount() > 0) {

			Shop selectedShop =  (Shop)shopListView.getSelectedItem();
			selectedShopId =  selectedShop.getId();
		}
		else {

			selectedShopId = null;
		}

		if (userListView.getCount() > 0) {

			User selectedUser =  (User)userListView.getSelectedItem();
			selectedUserId =  selectedUser.getId();
		}
		else {

			selectedUserId = null;
		}

	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		if(savedInstanceState.containsKey("SelectedShop"))
			selectedShopId = savedInstanceState.getInt("SelectedShop");

		if(savedInstanceState.containsKey("SelectedUser"))
			selectedUserId = savedInstanceState.getInt("SelectedUser");

	}

	private void setShopViewAsync() {

		setShopView(null);
		setUserView(null);

		AsyncTask<Void, Void, Void> shopViewTask =
				new AsyncTask<Void, Void, Void>() {

					List<Shop> shops;
					boolean hasError;

					@Override
					protected Void doInBackground(Void... params) {


						try {

							getShopFactory().reset();
							shops = getShopFactory().getShops();

							for (Shop shop : shops) {

								shop.toString();
							}
							hasError = false;

						} catch(Exception e) {

							hasError = true;
						}



						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						super.onPostExecute(result);

						if(hasError) {

							errorView.setText("Ошибка загрузки списка сотрудников. Проверьте настройки подключения.");
						} else {

							setShopView(shops);
						}
					}
				};

		shopViewTask.execute();
	}


	private void setShopView(List<Shop> shops) {

		shopListView.setOnItemSelectedListener(null);

		List<Shop> localShops = shops != null ? shops
				: new ArrayList<Shop>();
		ArrayAdapter<Shop> shopAdapter = new ArrayAdapter<Shop>
				(this,android.R.layout.simple_spinner_item, localShops);
		shopListView.setAdapter(shopAdapter);

		if(shops == null) {
			return;
		}

		if (selectedShopId != null) {
			int i = 0;
			for (Shop shop : localShops) {
				if(shop.getId() == selectedShopId.intValue())
					shopListView.setSelection(i);
				i++;
			}
		}

		shopListView.setOnItemSelectedListener(
				new OnItemSelectedListener() {



					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

						final Shop selectedShop = (Shop)shopListView.getSelectedItem();
						setUserViewAsync(selectedShop);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
	}


	protected void setUserViewAsync(final Shop selectedShop) {

		setUserView(null);

		AsyncTask<Void, Void, Void> userViewTask =
				new AsyncTask<Void, Void, Void>() {

					List<User> users;

					@Override
					protected Void doInBackground(Void... params) {

						users = selectedShop.getUsers();

						for (User user : users) {

							user.toString();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						super.onPostExecute(result);
						setUserView(users);
					}
				};

		userViewTask.execute();
	}


	protected void setUserView(List<User> users) {
		// TODO Auto-generated method stub
		List<User> localUsers = users != null ? users
				: new ArrayList<User>();
		ArrayAdapter<User> userAdapter = new ArrayAdapter<User>
				(this,android.R.layout.simple_spinner_item, localUsers);
		userListView.setAdapter(userAdapter);

		if(users == null) {
			return;
		}

		if (selectedUserId != null) {

			int i = 0;
			for (User user : localUsers) {
				if(user.getId() == selectedUserId.intValue())
					userListView.setSelection(i);
				i++;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingActivity.class);
				//intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);

				return true;
			case R.id.action_refresh:
				FixCurrentState();
				setShopViewAsync();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		getShopFactory().reset();
		super.onActivityResult(requestCode, resultCode, data);
	}
} 