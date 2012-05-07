package com.Legoing;

import java.util.List;

import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.UserItem;
import com.Legoing.UserControls.UCtrl_CataListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapter_forCataItem extends BaseAdapter{
	Context context;
	List<UserItem> listItems;
	LayoutInflater listContainter;
	
	public ListViewAdapter_forCataItem(Context context, List<UserItem> listItems)
	{
		super();
		this.context = context;
		this.listContainter = LayoutInflater.from(context);
		this.listItems = listItems;
		
		
	}
	public final class ListItemView
	{
		public ImageView imageView;
		public TextView textView_ItemID,textView_ItemName,textView_Year;
	}

	@Override
	public int getCount() {
		// TODO , Cxy, 2011-10-29 ����2:55:21
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO , Cxy, 2011-10-29 ����2:55:22
		return listItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO , Cxy, 2011-10-29 ����2:55:22
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO , Cxy, 2011-10-29 ����2:55:22
		UCtrl_CataListItem item = null;
		if (arg1 == null) {
			item = new UCtrl_CataListItem(context, null);
			arg1 = item;
			arg1.setTag(item);
		}else {
			item = (UCtrl_CataListItem)arg1.getTag();
		}
		item.setValue(listItems.get(arg0));
		return item;
	}
}
