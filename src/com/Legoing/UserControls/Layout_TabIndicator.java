package com.Legoing.UserControls;

import com.Legoing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Layout_TabIndicator extends RelativeLayout{

	Context context;
	ImageView imageView_word;
	public Layout_TabIndicator(Context context) {
		super(context);
		// TODO , Cxy, 2011-11-9 ÏÂÎç6:28:08
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.tab_indicator, null );
		
	}
	public void setWord(int resId)
	{
		imageView_word = (ImageView)findViewById(R.id.imageView_tab_indicator);
		imageView_word.setImageResource(resId);
	}

}
