package com.Legoing.UserControls;

import java.util.List;

import com.Legoing.R;
import com.Legoing.TreeViewAdapter_Cata;
import com.Legoing.ItemDef.UserItem;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

public class PopupWindow_MultiSelect extends PopupWindow{
	
	private Context mContext;
	private View mView;
	private ExpandableListView mExpandableListView;
	private TreeViewAdapter_Cata mTreeViewAdapter_Cata;
	private View.OnClickListener mOnClickListener_Confirm, mOnClickListener_Cancle;
	private Button mButton_Confirm, mButton_Cancle;
	public PopupWindow_MultiSelect(Context context)
	{
		this.mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.multi_select_layout, null);
		mExpandableListView = (ExpandableListView)mView.findViewById(R.id.expandableListView_selectSets);
		mButton_Cancle = (Button)mView.findViewById(R.id.button_popUpWin_cancle);
		mButton_Confirm = (Button)mView.findViewById(R.id.button_popUpWin_confirm);
		this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.setContentView(mView);
		this.setFocusable(true);
		
		this.mButton_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 9:13:30 PM
				if (mOnClickListener_Confirm != null) {
					mOnClickListener_Confirm.onClick(mView);
				}
				PopupWindow_MultiSelect.this.dismiss();
				
			}
		});
		this.mButton_Cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 9:14:57 PM
				if (mOnClickListener_Cancle != null) {
					mOnClickListener_Cancle.onClick(mView);
				}
				PopupWindow_MultiSelect.this.dismiss();
			}
		});
	}
	
	public void setValue(List<UserItem> items)
	{
		mTreeViewAdapter_Cata = TreeViewAdapter_Cata.getAdapterFromItems(mContext, items);
		mExpandableListView.setAdapter(mTreeViewAdapter_Cata);
	}
	
	public void show()
	{
		Resources res = mContext.getResources();
		this.setWidth( (int)res.getDimension( R.dimen.popwin_width));
		this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		Activity activity = (Activity)mContext;
		this.showAtLocation( activity.getWindow().getDecorView(), Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, 0);
		Animation slideLeftIn = AnimationUtils.loadAnimation(mContext,R.anim.slide_left_in);
		this.mView.startAnimation(slideLeftIn);
	}
	
	public PopupWindow_MultiSelect setPositiveButton(int resId, View.OnClickListener onClickListener)
	{
		this.mButton_Confirm.setText(resId);
		this.mOnClickListener_Confirm = onClickListener;
		return this;
	}
	public PopupWindow_MultiSelect setNegtiveButton(int resId, View.OnClickListener onClickListener)
	{
		this.mButton_Cancle.setText(resId);
		this.mOnClickListener_Cancle = onClickListener;
		return this;
	}

}
