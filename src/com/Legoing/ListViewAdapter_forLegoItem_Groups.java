package com.Legoing;

import java.util.LinkedList;
import java.util.List;

import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.Set;
import com.Legoing.UserControls.Layout_TitleLevel_2;
import com.Legoing.UserControls.UCtrl_LegoListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter_forLegoItem_Groups extends BaseAdapter {
	Context context;
	private List<LegoItem> allItems = new LinkedList<LegoItem>();
	List<LegoItem> sets, minifigs,parts;
	LayoutInflater listContainter;

	public ListViewAdapter_forLegoItem_Groups(Context context,
			List<LegoItem> sets, List<LegoItem> minifigs, List<LegoItem> parts) {
		super();
		this.context = context;
		this.listContainter = LayoutInflater.from(context);
		this.sets = sets;	
		this.minifigs = minifigs;
		this.parts = parts;
		allItems.add(Set.getDefaultSet());
		allItems.addAll(sets);
		
		allItems.add(LegoItem.getTestItem());
		allItems.addAll(minifigs);
		
		allItems.add(Set.getDefaultSet());
		allItems.addAll(parts);
	}

	public final class ListItemView {
		public ImageView imageView;
		public TextView textView_ItemID, textView_ItemName, textView_Year, textView_Cata;
	}

	@Override
	public int getCount() {
		// TODO , Cxy, 2011-10-29 下午2:55:21
		return allItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
//		if (arg0 == 0)// Tag:set
//		{
//			arg0++;
//		} 
//		if (arg0 == sets.size()+1) {
//			arg0++;
//		}
//		if (arg0 == minifigs.size()+1) {
//			arg0++;
//		}
		return allItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO , Cxy, 2011-10-29 下午2:55:22
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
			LegoItem cur = allItems.get(arg0);
//			listItemView.textView_ItemID.setText(cur.getNo());
//			listItemView.textView_ItemName.setText(cur.getName());
//			listItemView.textView_Cata.setText(cur.getCategory());
//			if (!(cur.isYearUnInitialized() || cur.isYearUnkonw())) {
//				listItemView.textView_Year.setText(cur.getYearBegin() + " - " + cur.getYearEnd());
//			}
			arg1 = new UCtrl_LegoListItem(context, null);
			((UCtrl_LegoListItem)arg1).setValue(cur);
			
		}
		return arg1;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO , Chen Xiaoyu Cxy, 2011-11-24 下午9:52:40
		return !(position == 0 || position == sets.size()+1 || position == sets.size()+1+minifigs.size()+1);
	}
}
