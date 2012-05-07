package com.Legoing.UserControls;

import com.Legoing.Activity_MainTab;
import com.Legoing.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class Layout_Home extends LinearLayout {

	Context context;

	public Layout_Home(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO , Cxy, 2011-10-30 ����3:38:39
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.home, this, true);

		initial_ID();
		initial();

	}

	void initial_ID() {

	}

	void initial() {
		// Type

	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    log("layout home dispaterKeyEvent:"+event.getKeyCode());
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			Activity_MainTab.shutdownConfirm(context);
//			return true;
		}
		return false;

	}
	private void log(String text)
	{
	    android.util.Log.d(TAG, text);
	}
	public static final String TAG = "Layout_Home";

}
