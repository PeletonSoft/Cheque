package com.peleton.cheque;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CustomEditText extends EditText
{

	private String oldText;

	
	public CustomEditText(Context context) {
		super(context);
		
		AcceptChanges();
		updateModified();
	}

	private void updateModified() {
		
		modified = false;
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				if (!oldText.equals(getText().toString())) {
					
					modified = true;
				}
			}
		});
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		AcceptChanges();
		updateModified();
	}

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		AcceptChanges();
		updateModified();
    }

    public void AcceptChanges() {
    	
    	this.oldText = getText().toString();
    	modified = false;
    }

    public void RejectChanges() {
    	
    	setText(this.oldText);
    	modified = false;
    }    
    
	@Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode())
        {
        	RejectChanges();
        	setSelection(getText().length());
        }
        return super.dispatchKeyEventPreIme(event);
    }

	private boolean modified;
	
	public boolean getModified() {
		
		return modified;
	}
}