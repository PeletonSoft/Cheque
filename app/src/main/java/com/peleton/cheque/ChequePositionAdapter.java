package com.peleton.cheque;

import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositions;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ChequePositionAdapter extends ArrayAdapter<ChequePosition> {

	Activity activity;
	int resource;
	ChequePositions data;
	ChequePosition[] dataArray;
	CustomEditText newPositionInput;
	private ChequeRefreshNotify notify;
	public ChequePositionAdapter(Context context, int resource,
			ChequePositions data, CustomEditText newPositionInput, 
			ChequeRefreshNotify notify, AsyncTask<ChequePosition, Void, Void> deleteTask) {
		
		super(context, resource, data.toArray());
		this.resource = resource;
		this.activity = (Activity)context;
		this.data = data;
		this.dataArray = data.toArray();
		this.newPositionInput = newPositionInput;
		this.notify = notify;
		
	}
	
	private class CashFrame {
		
		int cashPosition;
		CustomEditText cashQuantView;
        CustomEditText cashDiscountView;
        ChequePosition cashChequePosition;
        View cashRow;
	}
	
	int SET_FOCUS_LATENCY = 40;
	CashFrame cashFrame;
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = activity.getLayoutInflater();
        final View row = inflater.inflate(resource, parent, false);

        final ChequePosition chequePosition = dataArray[position];
        
        final CustomEditText quantView = (CustomEditText) row.findViewById(R.id.quant);
        final CustomEditText discountView = (CustomEditText) row.findViewById(R.id.discount);        
        
        setViewValue(row, chequePosition);
		
        quantView.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(event == null)
					return false;
				
				if(event.getKeyCode() != KeyEvent.KEYCODE_ENTER)
					return false;

				if (!updateQuant(row, chequePosition)) {
				
					getRequestFocusAsync(discountView, SET_FOCUS_LATENCY).execute();
				}
				
				return true;
			}

        });


        quantView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus && quantView.getModified()) {
					
					updateQuant(row, chequePosition);
				}
					
				
			}
		});

        discountView.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(event == null)
					return false;
				
				if(event.getKeyCode() != KeyEvent.KEYCODE_ENTER)
					return false;

				updateDiscount(row, chequePosition);
				
				if(position == dataArray.length - 1) {
					
					getRequestFocusAsync(newPositionInput, SET_FOCUS_LATENCY).execute();
				}
				
				return true;
			}

        });
        

        discountView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				if(!hasFocus && discountView.getModified())  {
					
					updateDiscount(row, chequePosition);
				}
			}
		});

        final CashFrame cashFrame = this.cashFrame;
        if(cashFrame != null && cashFrame.cashPosition == position - 1) {
        	
        	cashFrame.cashDiscountView.setOnEditorActionListener(new OnEditorActionListener() {
    			
    			@Override
    			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

    				if(event == null)
    					return false;
    				
    				if(event.getKeyCode() != KeyEvent.KEYCODE_ENTER)
    					return false;
    				
    				
    				if (!updateDiscount(cashFrame.cashRow, cashFrame.cashChequePosition)) {
    					
    					getRequestFocusAsync(quantView, SET_FOCUS_LATENCY).execute();
    				}
    				
    				return true;
    			}

            });
        }

        this.cashFrame = new CashFrame() {{
        	cashChequePosition = chequePosition;
        	cashDiscountView = discountView;
        	cashQuantView = quantView;
        	cashPosition = position;
        	cashRow = row;
        }};
        
		return row;
	}

	private void setViewValue(
			final View row,
			final ChequePosition chequePosition) {

        TextView articleView = (TextView) row.findViewById(R.id.article);
        CustomEditText quantView = (CustomEditText) row.findViewById(R.id.quant);
        CustomEditText discountView = (CustomEditText) row.findViewById(R.id.discount);        
        TextView priceView = (TextView) row.findViewById(R.id.price);
        
        articleView.setText(Integer.toString(chequePosition.getArticleId()));
        
        quantView.setText(String.format("%1.2f", chequePosition.getQuant()).replace(',', '.'));
        quantView.AcceptChanges();
        
        discountView.setText(String.format("%1.2f", chequePosition.getDiscount()).replace(',', '.'));
        discountView.AcceptChanges();
        
        priceView.setText(String.format("%1.2f", chequePosition.getPrice()));
        //priceView.setText(Integer.toString(chequePosition.getId()));
		
	}

	private AsyncTask<Void, Void, Void> getRequestFocusAsync(
			final CustomEditText editText, final int latency) {
		return new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(latency);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
				return null;
			}
			
		    @Override
		    protected void onPostExecute(Void result) {
		    	
		    	editText.requestFocus();
		    	editText.selectAll();
		    	InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);		    	
		    }
			
		};
	}

	private boolean updateQuant(final View row,
			final ChequePosition chequePosition) {
		
		CustomEditText quantView = (CustomEditText) row.findViewById(R.id.quant);	

		if(quantView.getModified()) {
			
			final double quant;
			
			try {
				String quantText = quantView.getText().toString();
				quant = Double.parseDouble(quantText);
			} catch(NumberFormatException e) {
				
				getRequestFocusAsync(quantView, SET_FOCUS_LATENCY).execute();
				return true;
			}
			
			AsyncTask<Void,Void,Void> quantTask = new AsyncTask<Void, Void, Void>() {

				boolean isNeedRefresh;
				
				@Override
				protected Void doInBackground(Void... params) {
					
					isNeedRefresh = false;
					if(quant != chequePosition.getQuant()) {
					
						chequePosition.setQuant(quant);
						isNeedRefresh = true;
					}
					
					
					return null;
				}
				
			    @Override
			    protected void onPostExecute(Void result) {
			    	
			    	if(isNeedRefresh) {
			    		
			    		setViewValue(row, chequePosition);
			    		notify.onRefresh();
			    	}
			    }

			};
			
			quantTask.execute();			
		}

		return false;
	}	

	private boolean updateDiscount(final View row,
			final ChequePosition chequePosition) {

		CustomEditText discountView = (CustomEditText) row.findViewById(R.id.discount);		
		final double discount;
		
		if (discountView.getModified()) {
			
			try {
				
				String discountText = discountView.getText().toString();
				discount = Double.parseDouble(discountText);
			} catch(NumberFormatException e) {
				
				getRequestFocusAsync(discountView, SET_FOCUS_LATENCY).execute();
				return true;
			}
			
			AsyncTask<Void,Void,Void> discountTask = new AsyncTask<Void, Void, Void>() {

				boolean isNeedRefresh;
				@Override
				protected Void doInBackground(Void... params) {
					
					isNeedRefresh = false;
					if(discount != chequePosition.getDiscount()) {
					
						chequePosition.setDiscount(discount);
						isNeedRefresh = true;
					}
					
					
					return null;
				}
				
			    @Override
			    protected void onPostExecute(Void result) {
			    	
			    	if(isNeedRefresh) {
			    		
			    		setViewValue(row, chequePosition);
			    		notify.onRefresh();
			    	}
			    }
			};
			discountTask.execute();
		}
		
		return false;
	}	
	
	public void selectLast() {
		final CashFrame cashFrame = this.cashFrame;
        if(cashFrame != null && cashFrame.cashPosition == dataArray.length - 1)	{
        	getRequestFocusAsync(cashFrame.cashQuantView, SET_FOCUS_LATENCY).execute();
        }
	}
}
