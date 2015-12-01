package com.peleton.cheque;

import java.util.List;

import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.StoreArticle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity {

	private int shopId;	
	private int userId;
	private String password;

	private EditText filterView;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		shopId = getIntent().getIntExtra("shopId", 3);
		userId = getIntent().getIntExtra("userId", 1);
		password = getIntent().getStringExtra("password");
		
		filterView = (EditText)findViewById(R.id.filter);
		ImageButton searchView =  (ImageButton)findViewById(R.id.search);
		listView = (ListView)findViewById(R.id.list);
		
		searchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				search();
				
			}
		});
		
		filterView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            search();
		            return true;
		        }
		        return false;
			}
		});
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

    private Shop shop;
    
    private Shop getShop() {
    	
    	if(shop == null) {
    		
    		shop = getShopFactory().createShop(shopId);
    	}
    	
		return shop;
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
	protected void onResume() {
		// TODO Auto-generated method stub
		viewKeyboard(filterView);
		super.onResume();
	}

	private void search() {
		final String filter = filterView.getText().toString();
		if(filter == null || filter.length() < 3) {
			
			return;
		}
		
		AsyncTask<Void, Void, Void> searchTask = new AsyncTask<Void, Void, Void>() {

			
			SearchByArticleAdapter adapter;
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				List<StoreArticle> list = getShop().searchByArticle(filter);
				StoreArticle[] array = list.toArray(new StoreArticle[0]);
				adapter = new SearchByArticleAdapter(SearchActivity.this, R.layout.search_view, array);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				listView.setAdapter(adapter);
				super.onPostExecute(result);
			}
		};
		searchTask.execute();
	}
}
