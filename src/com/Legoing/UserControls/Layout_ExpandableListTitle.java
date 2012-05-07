package com.Legoing.UserControls;

import com.Legoing.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Layout_ExpandableListTitle extends RelativeLayout {

	String titleText;
	String titleText2;

	public Layout_ExpandableListTitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO , Cxy, 2011-11-24  5:16:05
	}

	public Layout_ExpandableListTitle(Context context) {
		super(context);
		// TODO , Cxy, 2011-11-24  5:16:05
		LayoutInflater.from(context)
				.inflate(R.layout.expandable_list_title, this, true);
	}

	public Layout_ExpandableListTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO , Cxy, 2011-11-24  5:08:44
		LayoutInflater.from(context)
				.inflate(R.layout.expandable_list_title, this, true);
		if (attrs != null) {
			TypedArray tArray = context.obtainStyledAttributes(attrs,
					R.styleable.Layout_TitleLevel_2);
			setTitleText(tArray
					.getString(R.styleable.Layout_TitleLevel_2_mTitleText));
			setTitleText2(tArray
					.getString(R.styleable.Layout_TitleLevel_2_mTitleText2));
			tArray.recycle();
		}

	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {

		this.titleText = titleText;
		if (titleText != null)
			((TextView) findViewById(R.id.textView_ULayout_expandableListTitle_titleText))
					.setText(this.titleText);
	}

	public String getTitleText2() {
		return titleText2;
	}

	public void setTitleText2(String titleText2) {

		this.titleText2 = titleText2;
		if (titleText2 != null)
			((TextView) findViewById(R.id.textView_ULayout_expandableListTitle_titleText2))
					.setText(titleText2);
	}
}
