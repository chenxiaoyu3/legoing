package com.Legoing;

import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.IComponentItem;

import com.Legoing.UserControls.Layout_TitleLevel_2;
import com.Legoing.UserControls.UCtrl_LegoComponentListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

public class ListViewAdapter_forComponents_Groups extends BaseAdapter {

	Context context;
	LayoutInflater listContainter;
	List<ComponentItem> sets, minifigs,parts;
	private List<ComponentItem> allItems = new LinkedList<ComponentItem>();
	public ListViewAdapter_forComponents_Groups(Context context,
			List<ComponentItem> sets, List<ComponentItem> minifigs,
			List<ComponentItem> parts) {
		this.context = context;
		this.listContainter = LayoutInflater.from(context);
		this.sets = sets;	
		this.minifigs = minifigs;
		this.parts = parts;
		allItems.add(ComponentItem.getTest());
		allItems.addAll(sets);
		
		allItems.add(ComponentItem.getTest());
		allItems.addAll(minifigs);
		
		allItems.add(ComponentItem.getTest());
		allItems.addAll(parts);
	}

	@Override
	public int getCount() {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:20:50
		return allItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:20:51
		return allItems.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:20:51
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup parent) {
		// TODO , Chen Xiaoyu Cxy, 2012-1-1 ����1:20:51
		if (arg0 == 0)// Tag:set
		{
			//arg1 = listContainter.inflate(R.layout.title_level_2, null);
			// ((Layout_TitleLevel_2)arg1).setTitleText("SSSSet");
			 arg1 = new Layout_TitleLevel_2(context);
			 ((Layout_TitleLevel_2)arg1).setTitleText("Set");
			 ((Layout_TitleLevel_2)arg1).setTitleText2(String.valueOf(sets.size()));
			 
		} else if (arg0 == sets.size()+1) {
			arg1 = new Layout_TitleLevel_2(context);
			((Layout_TitleLevel_2)arg1).setTitleText("Minifig");
			((Layout_TitleLevel_2)arg1).setTitleText2(String.valueOf(minifigs.size()));
		} else if (arg0 == sets.size()+1+minifigs.size()+1)
		{
			arg1 = new Layout_TitleLevel_2(context);
			((Layout_TitleLevel_2)arg1).setTitleText("Part");
			((Layout_TitleLevel_2)arg1).setTitleText2(String.valueOf(parts.size()));
		}else
		{

//			ListItemView listItemView = null;
//			///arg1.
//			listItemView = new ListItemView();
//			arg1 = listContainter.inflate(R.layout.list_item, null);
//			listItemView.imageView = (ImageView) arg1
//					.findViewById(R.id.IB_ListItem);
//			listItemView.textView_ItemID = (TextView) arg1
//					.findViewById(R.id.TV_ListItem_ItemID);
//			listItemView.textView_ItemName = (TextView) arg1
//					.findViewById(R.id.TV_ListItem_ItemName);
//			listItemView.textView_Year = (TextView)arg1.findViewById(R.id.TV_ListItem_Year);
//			listItemView.textView_Cata = (TextView)arg1.findViewById(R.id.TV_ListItem_Catalog);
//			
			ComponentItem cur = allItems.get(arg0);
//			listItemView.textView_ItemID.setText(cur.getNo());
//			listItemView.textView_ItemName.setText(cur.getName());
//			listItemView.textView_Cata.setText(cur.getCategory());
//			if (!(cur.isYearUnInitialized() || cur.isYearUnkonw())) {
//				listItemView.textView_Year.setText(cur.getYearBegin() + " - " + cur.getYearEnd());
//			}
			arg1 = new UCtrl_LegoComponentListItem(context, null);
			((UCtrl_LegoComponentListItem)arg1).setValue(cur);
			
		}
		return arg1;
	}

}
