package com.Legoing.UserControls;

import java.util.List;

import com.Legoing.R;
import com.Legoing.TreeViewAdapter_Cata;
import com.Legoing.ItemDef.UserItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

public class Layout_ItemSelection extends LinearLayout{

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private TreeViewAdapter_Cata mTreeViewAdapter_Cata;
	OnClickListener mOnClickListener;
	public Layout_ItemSelection(Context context) {
		super(context);
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 1:21:52 PM
		LayoutInflater.from(context).inflate(R.layout.multi_select_layout, this);
		this.mContext = context;
		mExpandableListView = (ExpandableListView)findViewById(R.id.expandableListView_selectSets);
	}
	
	public void setValue(List<UserItem> items)
	{
		mTreeViewAdapter_Cata = TreeViewAdapter_Cata.getAdapterFromItems(mContext, items);
		mExpandableListView.setAdapter(mTreeViewAdapter_Cata);
	}
	

}
