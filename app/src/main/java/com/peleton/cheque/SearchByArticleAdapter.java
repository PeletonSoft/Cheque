package com.peleton.cheque;

import com.peleton.cheque.model.StoreArticle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchByArticleAdapter  extends ArrayAdapter<StoreArticle> {

	private int resource;
	private Activity activity;
	private StoreArticle[] data;

	public SearchByArticleAdapter(Context context, int resource,
			StoreArticle[] data) {
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
        final StoreArticle storeArticle = data[position];
        
        TextView articleIdView = (TextView)row.findViewById(R.id.article_id);
        TextView infoView = (TextView)row.findViewById(R.id.info);
        TextView quantView = (TextView)row.findViewById(R.id.quant);
        TextView storeView = (TextView)row.findViewById(R.id.store);
        ImageButton selectView = (ImageButton)row.findViewById(R.id.select);
        
        articleIdView.setText(String.valueOf(storeArticle.getArticleId()));
        articleIdView.setText(String.valueOf(storeArticle.getArticleId()));
        infoView.setText(storeArticle.getArticle() + "\n" + storeArticle.getDescription());
        storeView.setText(storeArticle.getStore());
        quantView.setText(String.format("%1.2f",storeArticle.getQuant()));
        
        selectView.setEnabled(storeArticle.getQuant() >= 0.005f);
        selectView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
	    		intent.putExtra("articleId", storeArticle.getArticleId());
	    		activity.setResult(Activity.RESULT_OK, intent);
				activity.finish();
			}
		});		

		return row;
	}
}
