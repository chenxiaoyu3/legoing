package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.StaticOverall;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UCtrl_ColorPickerItem extends LinearLayout{

	static int cover = 0xff000000;
	int colorIndex;
	public int getColorIndex() {
		return colorIndex;
	}
	public UCtrl_ColorPickerItem(Context context)
	{
		super(context);
		// TODO , Cxy, 2012-2-15 下午2:33:40
		LayoutInflater.from(context).inflate(R.layout.color_picker_item, this, true);
	}
	public UCtrl_ColorPickerItem(Context context, int colorIndex) {
		super(context);
		// TODO , Cxy, 2012-2-15 下午2:33:40
		LayoutInflater.from(context).inflate(R.layout.color_picker_item, this, true);
		setColorIndex(colorIndex);
	}
	public void setColorIndex(int colorIndex)
	{
		TextView textView_Color = (TextView)findViewById(R.id.textView_colorPickerItem_Color);
		TextView textView_Text = (TextView)findViewById(R.id.textView_colorPickerItem_Text);
		this.colorIndex = colorIndex;
		int colorValue = StaticOverall.legoColors.getValue(colorIndex);
		int color = cover | colorValue;
		textView_Color.setBackgroundColor(color);
		textView_Text.setText(StaticOverall.legoColors.toString(colorIndex));
	}
	
}
