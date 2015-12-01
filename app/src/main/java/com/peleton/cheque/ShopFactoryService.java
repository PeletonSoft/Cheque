package com.peleton.cheque;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.peleton.cheque.model.Authentication;
import com.peleton.cheque.model.ShopFactory;
//import com.peleton.cheque.model.fake.FakeShopFactory;
import com.peleton.cheque.model.http.HttpShopFactory;

public class ShopFactoryService {

	
	private Activity activity;
	
	public ShopFactoryService(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}
	
	public ShopFactory createShopFactory(Authentication authentication) {
	
		Resources resources = activity.getResources();
		Context context = activity.getBaseContext();
		
		SharedPreferences settings = 
				PreferenceManager.getDefaultSharedPreferences(context);
		
		String server = "";
		String key = "";
		String theme = "";
		int defaultShopId = 0;
		
		try {
			
			server = settings.getString(
					resources.getString(R.string.preferences_server), "");
			
		} catch(Exception e) { 
			
			Log.i("asfcsdfvc-server",e.toString());
		}

		try {
			
			key = settings.getString(
					resources.getString(R.string.preferences_key), "");
			
		} catch(Exception e) { 
			
			Log.i("asfcsdfvc-key",e.toString());
		}
		
		try {
			
			theme = settings.getString(
					resources.getString(R.string.preferences_theme), "");
			
		} catch(Exception e) { 
			
			Log.i("asfcsdfvc-theme",e.toString());
		}
		
		try {
			
			defaultShopId = settings.getInt(
					resources.getString(R.string.preferences_shop), 0);
			
		} catch(Exception e) { 
			
			Log.i("asfcsdfvc-shop",e.toString());
		}		
		//return new FakeShopFactory();
		
		return new HttpShopFactory(server,key,theme,defaultShopId,authentication);
		
		
	}
}
