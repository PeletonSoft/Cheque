package com.peleton.cheque;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.SendResult;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.StoreInfoFindResult;
import com.peleton.cheque.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Shop shop;
	private User user;
	private Cheque cheque;


	CustomEditText newPositionInput;
	TextView errorView;
	ListView positionsView;
	TextView numberView;
	TextView sumView;

	ChequeRefreshNotify notify = new ChequeRefreshNotify() {

		@Override
		public void onRefresh() {

			refreshState();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		signed = false;
		setContentView(R.layout.activity_main);

		errorView = (TextView)findViewById(R.id.error);
		positionsView = (ListView)findViewById(R.id.positions);

		numberView = (TextView)findViewById(R.id.number);
		sumView = (TextView)findViewById(R.id.sum);


		newPositionInput = (CustomEditText)findViewById(R.id.new_position);
		newPositionInput.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId != EditorInfo.IME_ACTION_DONE &&
						(event == null || event.getKeyCode() != KeyEvent.KEYCODE_ENTER))
					return false;

				if(!newPositionInput.getModified())
					return false;

				tryNewPositionAsync();

				return true;

			}
		});

		newPositionInput.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus && newPositionInput.getModified()) {

					tryNewPositionAsync();
				}

			}
		});

		registerForContextMenu(positionsView);


		SwipeDismissListViewTouchListener touchListener =
				new SwipeDismissListViewTouchListener(
						positionsView,
						new SwipeDismissListViewTouchListener.DismissCallbacks() {
							@Override
							public boolean canDismiss(int position) {
								return true;
							}

							@Override
							public void onDismiss(ListView listView, int[] reverseSortedPositions) {
								for (int position : reverseSortedPositions) {
									ChequePosition chequePosition = cheque.getChequePositions().toArray()[position];
									deletePositionAsync(chequePosition);
								}
							}
						});
		positionsView.setOnTouchListener(touchListener);

		ImageButton scanView = (ImageButton)findViewById(R.id.scan);
		scanView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
				scanIntegrator.initiateScan();
			}
		});

		ImageButton searchView = (ImageButton)findViewById(R.id.search);
		searchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
				intent.putExtra("shopId", shopId);
				intent.putExtra("userId", userId);
				intent.putExtra("password", password);
				startActivityForResult(intent, SearchActivity.class.hashCode());

			}
		});

	}

	private int shopId;
	private int userId;
	private String password;
	boolean isLogined;
	private boolean signed;

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

		super.onResume();

		if(!signed) {

			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivityForResult(intent, LoginActivity.class.hashCode());

			return;
		}

		refreshList();
	}

	private void refreshList() {

		newPositionInput.setText("");
		newPositionInput.AcceptChanges();

		AsyncTask<Void, Void, Void> getLastChequeTask =
				new AsyncTask<Void, Void, Void>() {

					ChequePositionAdapter adapter;

					@Override
					protected Void doInBackground(Void... params) {


						cheque = getShopFactory().getLastCheque(getShop(), getUser());
						cheque.getChequePositions();

						for (ChequePosition position : cheque.getChequePositions()) {

							position.getQuant();
						}

						adapter = getPositionsAdapter();

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						positionsView.setAdapter(adapter);
						refreshState();
						viewKeyboard(newPositionInput);
						super.onPostExecute(result);
					}
				};

		getLastChequeTask.execute();

	}

	private void refreshState() {

		AsyncTask<Void, Void, Void>  updateStateTask =
				new AsyncTask<Void, Void, Void>() {

					double sum;
					Integer number;
					@Override
					protected Void doInBackground(Void... params) {

						sum = cheque.getSum();
						number = cheque.getNumber();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {

						sumView.setText(String.format("%1.2f", sum));
						numberView.setText(number == null ? "" : "Чек №" + number.toString());
						super.onPostExecute(result);
					}
				};

		updateStateTask.execute();

	}

	private void viewKeyboard(final View v) {

		AsyncTask<Void, Void, Void> viewKeyboardTask =
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						v.requestFocus();
						InputMethodManager imm =
								(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput
								(v, InputMethodManager.SHOW_IMPLICIT);

						super.onPostExecute(result);
					}
				};

		viewKeyboardTask.execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);


		if (requestCode == LoginActivity.class.hashCode()) {

			switch (resultCode) {

				case Activity.RESULT_OK:


					shopId = data.getIntExtra("shopId", 1);
					userId = data.getIntExtra("userId", 1);
					password = data.getStringExtra("password");
					signed = true;

					break;

				case Activity.RESULT_CANCELED:

					finish();
					break;

			}
		}

		if (requestCode == SelectActivity.class.hashCode()) {

			switch (resultCode) {

				case Activity.RESULT_OK:

					final int storeInfoId = data.getIntExtra("storeInfoId", 0);
					AsyncTask<Void, Void, Void> addTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							addNewPosition(storeInfoId);
							super.onPostExecute(result);
						}
					};
					addTask.execute();

					break;
			}
		}

		if(requestCode == IntentIntegrator.REQUEST_CODE) {

			IntentResult scanningResult =
					IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if(scanningResult != null) {

				String scanContent = scanningResult.getContents();
				if(scanContent != null && scanContent.length() >= 5) {

					try {
						final int articleId = Integer.parseInt(scanContent.substring(0, 5));

						insertArticleIdAsync(articleId);

					}
					catch (Exception e) {
						errorView.setText("Штрих код не удалось распознать");
					}

				}
			}
		}

		if(requestCode == SearchActivity.class.hashCode()) {

			switch (resultCode) {

				case Activity.RESULT_OK:

					final int articleId = data.getIntExtra("articleId", 0);
					insertArticleIdAsync(articleId);

					break;

			}
		}
	}

	private void insertArticleIdAsync(final int articleId) {
		AsyncTask<Void, Void, Void> insertTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				newPositionInput.setText(String.valueOf(articleId));
				tryNewPositionAsync();
				super.onPostExecute(result);
			}
		};

		insertTask.execute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putInt("shopId", shopId);
		outState.putInt("userId", userId);
		outState.putString("password", password);
		outState.putBoolean("signed", signed);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		if(savedInstanceState.containsKey("shopId"))
			shopId = savedInstanceState.getInt("shopId");

		if(savedInstanceState.containsKey("userId"))
			userId = savedInstanceState.getInt("userId");

		if(savedInstanceState.containsKey("password"))
			password = savedInstanceState.getString("password");

		if(savedInstanceState.containsKey("signed"))
			signed = savedInstanceState.getBoolean("signed");

	}

	private void tryNewPositionAsync() {

		if(cheque == null) {

			errorView.setText("Не удалось загрузить информацию о чеке с сервера.");
			return;
		}


		String articleIdText;
		try {

			articleIdText = newPositionInput.getText().toString();
			newPositionInput.RejectChanges();
		}
		catch (NumberFormatException e) {

			errorView.setText("Код артикула введен некорректно.");
			newPositionInput.RejectChanges();
			viewKeyboard(newPositionInput);
			return;
		}

		final int articleId = Integer.parseInt(articleIdText);

		AsyncTask<Void, Void,Void> addNewPositionTask = new AsyncTask<Void, Void, Void>() {

			StoreInfoFindResult storeInfoFindResult;

			@Override
			protected Void doInBackground(Void... params) {

				storeInfoFindResult = shop.getStoreInfo(articleId);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				errorView.setText("");
				if(storeInfoFindResult.isError() ||
						storeInfoFindResult.isWarning()) {

					errorView.setText(storeInfoFindResult.getMessage());
				}

				if(storeInfoFindResult.isError()) {

					viewKeyboard(newPositionInput);
					return;
				}

				if(!storeInfoFindResult.isSingle()) {

					Intent intent = new Intent(MainActivity.this, SelectActivity.class);
					intent.putExtra("articleId", articleId);
					intent.putExtra("userId", userId);
					intent.putExtra("shopId", shopId);
					intent.putExtra("password", password);
					startActivityForResult(intent, SelectActivity.class.hashCode());
					return;
				}

				addNewPosition(storeInfoFindResult.getStoreInfoId());

			}

		};
		addNewPositionTask.execute();

	}

	private void addNewPosition(final int storeInfoId) {

		AsyncTask<Void, Void,Void> addNewPositionTask = new AsyncTask<Void, Void, Void>() {
			ChequePositionAdapter adapter;

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				cheque.getChequePositions().add(storeInfoId);
				adapter = getPositionsAdapter();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				positionsView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				selectLast(adapter);
				refreshState();
				super.onPostExecute(result);
			}
		};
		addNewPositionTask.execute();
	}

	private void selectLast(final ChequePositionAdapter adapter) {
		AsyncTask<Void, Void, Void> selectLastTask =
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(70);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						// TODO Auto-generated method stub
						adapter.selectLast();
						super.onPostExecute(result);
					}
				};
		selectLastTask.execute();
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cheque, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {


		switch (item.getItemId()){
			case R.id.action_delete:

				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
							case DialogInterface.BUTTON_POSITIVE:

								ChequePosition[] positions = cheque.getChequePositions().toArray();
								AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();

								int position = info.position;
								deletePositionAsync(positions[position]);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								//No button clicked
								break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Для удаления позиции нажмите кнопку ДА.")
						.setPositiveButton("Да", dialogClickListener)
						.setNegativeButton("Нет", dialogClickListener)
						.show();



				return true;
			case R.id.action_info:

				ChequePosition[] positions = cheque.getChequePositions().toArray();
				AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
				int position = info.position;


				Intent intent = new Intent(MainActivity.this, InfoActivity.class);
				intent.putExtra("userId", userId);
				intent.putExtra("shopId", shopId);
				intent.putExtra("password", password);
				intent.putExtra("positionId", positions[position].getId());
				startActivityForResult(intent, InfoActivity.class.hashCode());
				return true;
			default:

				return super.onContextItemSelected(item);
		}

	}

	private Shop getShop() {
		if(shop == null) {

			shop = 	getShopFactory().createShop(shopId);
		}
		return shop;
	}

	private User getUser() {

		if (user == null) {

			user = getShopFactory().createUser(userId);
		}
		return user;
	}

	private void deletePositionAsync(final ChequePosition chequePosition) {

		getDeleteTask().execute(chequePosition);

	}

	private AsyncTask<ChequePosition, Void, Void> getDeleteTask() {
		return new AsyncTask<ChequePosition, Void, Void>() {

			ChequePositionAdapter adapter;
			@Override
			protected Void doInBackground(ChequePosition... params) {

				cheque.getChequePositions().delete(params[0]);
				adapter = getPositionsAdapter();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				positionsView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				refreshState();

				super.onPostExecute(result);
			}

		};
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent settingIntent = new Intent(this, SettingActivity.class);
				startActivity(settingIntent);

				return true;

			case R.id.action_revise:
				Intent reviseIntent = new Intent(this, ReviseActivity.class);
				reviseIntent.putExtra("shopId", shopId);
				reviseIntent.putExtra("userId", userId);
				reviseIntent.putExtra("password", password);
				startActivity(reviseIntent);

				return true;

			case R.id.action_refresh:
				getShopFactory().reset();
				refreshList();
				errorView.setText("");
				break;

			case R.id.action_send :

				DialogInterface.OnClickListener sendDialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch (which){
							case DialogInterface.BUTTON_POSITIVE:

								sendCheque();
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								//No button clicked
								break;
						}
					}
				};

				AlertDialog.Builder sendBuilder = new AlertDialog.Builder(this);
				sendBuilder.setMessage("Для отправки чека нажмите кнопку ДА.")
						.setPositiveButton("Да", sendDialogClickListener)
						.setNegativeButton("Нет", sendDialogClickListener)
						.show();
				break;

			case R.id.action_clear :

				DialogInterface.OnClickListener clearDialogClickListener =
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								switch (which){

									case DialogInterface.BUTTON_POSITIVE:

										clearCheque();
										break;

									case DialogInterface.BUTTON_NEGATIVE:

										break;
								}
							}
						};

				AlertDialog.Builder clearBuilder = new AlertDialog.Builder(this);
				clearBuilder.setMessage("Для очистки чека нажмите кнопку ДА.")
						.setPositiveButton("Да", clearDialogClickListener)
						.setNegativeButton("Нет", clearDialogClickListener)
						.show();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void sendCheque() {

		AsyncTask<Void, Void, Void> sendTask = new AsyncTask<Void, Void, Void>() {

			SendResult sendResult;
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				sendResult = cheque.send();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				refreshList();
				errorView.setText(sendResult.getMessage());
				super.onPostExecute(result);
			}
		};
		sendTask.execute();
	}

	protected void clearCheque() {

		AsyncTask<Void, Void, Void> sendTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				cheque.clear();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				refreshList();
				errorView.setText("Чек был очищен");
				super.onPostExecute(result);
			}
		};
		sendTask.execute();
	}

	private ChequePositionAdapter getPositionsAdapter() {
		return new ChequePositionAdapter(
				MainActivity.this,
				R.layout.cheque_view,
				cheque.getChequePositions(),
				newPositionInput, notify,
				getDeleteTask());
	}

	boolean doubleBackToExitPressedOnce = false;

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Нажмите кнопку BACK еще раз для выхода.", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce=false;

			}
		}, 5000);
	}
}

