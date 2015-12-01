package com.peleton.cheque;

import com.peleton.cheque.model.StoreInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


public class SelectStoreInfoAdapter extends ArrayAdapter<StoreInfo> {

	private int resource;
	private Activity activity;
	private StoreInfo[] data;

	public SelectStoreInfoAdapter(Context context, int resource,
			StoreInfo[] data) {
		// TODO Auto-generated constructor stub
		super(context, resource, data);
		
		this.resource = resource;
		this.activity = (Activity)context;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();
        final View row = inflater.inflate(resource, parent, false);
        final StoreInfo storeInfo = data[position];
        
        TextView infoView = (TextView)row.findViewById(R.id.info);
        TextView storeView = (TextView)row.findViewById(R.id.store);
        TextView quantView = (TextView)row.findViewById(R.id.quant);
        ImageButton selectView = (ImageButton)row.findViewById(R.id.select);
        
        infoView.setText(storeInfo.getAttr() + ": " + storeInfo.getAttrInfo());
        storeView.setText(storeInfo.getStore());
        quantView.setText(String.format("%1.2f",storeInfo.getQuant()));
        
        selectView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
	    		intent.putExtra("storeInfoId", storeInfo.getId());
	    		activity.setResult(Activity.RESULT_OK, intent);
				activity.finish();
			}
		});
        
        return row;
	}
}
