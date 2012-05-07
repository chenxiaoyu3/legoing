package com.Legoing;

import java.util.List;
import java.util.Map;

import com.Legoing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ListViewAdapter extends BaseAdapter {
	Context context;
	List<Map<String,Object>> listItems;
	LayoutInflater listContainter;
	
	public ListViewAdapter(Context context, List<Map<String, Object>> listItems)
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
		// TODO , Cxy, 2011-10-29 下午2:55:21
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
		ListItemView listItemView = null;
		if(arg1 == null)
		{
			listItemView = new ListItemView();
			arg1 = listContainter.inflate(R.layout.list_item, null);
			listItemView.imageView = (ImageView)arg1.findViewById(R.id.IB_ListItem);
			listItemView.textView_ItemID = (TextView)arg1.findViewById(R.id.TV_ListItem_ItemID);
			listItemView.textView_ItemName = (TextView)arg1.findViewById(R.id.TV_ListItem_ItemName);
			arg1.setTag(listItemView);
		}else
		{
			listItemView = (ListItemView)arg1.getTag();
		}
		//Map<String, Object> cur = listItems.get(arg0);
		//listItemView.textView_ItemID.setText(cur.get(Set.getName_SetID()).toString());
		//listItemView.textView_ItemName.setText(cur.get(Set.getName_SetName()).toString());
		return arg1;
	}

}
