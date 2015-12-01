package com.peleton.cheque;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		final Resources resources = getResources();		
		final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
		
		final EditText serverView = (EditText)findViewById(R.id.server);
		final EditText keyView = (EditText)findViewById(R.id.key);
		final EditText themeView = (EditText)findViewById(R.id.theme);
		final EditText shopView = (EditText)findViewById(R.id.shop);

		try {
		
			serverView.setText(settings.getString(
					resources.getString(R.string.preferences_server), "192.168.1.6:8080"));
		} catch(Exception e) {
			
			serverView.setText("192.168.1.6:8080");
		}
		
		try {
			
			keyView.setText(settings.getString(
					resources.getString(R.string.preferences_key), ""));
		} catch(Exception e) {
			
			keyView.setText("");
		}
		
		try {
		
			themeView.setText(settings.getString(
					resources.getString(R.string.preferences_theme), "androidV"));
		} catch(Exception e) {
			
			themeView.setText("androidV");
		}
		
		try {
			
			int shopId = settings.getInt(
					resources.getString(R.string.preferences_shop), 1);
			shopView.setText(Integer.valueOf(shopId).toString());
		} catch(Exception e) {
			
			shopView.setText("1");
		}
		
		Button saveButton = (Button)findViewById(R.id.save);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences.Editor editor = settings.edit();
				
				editor.putString(
						resources.getString(R.string.preferences_server), 
						serverView.getText().toString());
				
				editor.putString(
						resources.getString(R.string.preferences_key), 
						keyView.getText().toString());
				editor.putString(
						resources.getString(R.string.preferences_theme), 
						themeView.getText().toString());
				
				String shopIdText = shopView.getText().toString();
				
				editor.putInt(resources.getString(R.string.preferences_shop), 
						Integer.parseInt(shopIdText));				
				editor.commit();
				
				finish();
			}
		});
		
		
	}

}

