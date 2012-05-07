package com.Legoing;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.UserControls.UCtrl_LegoComponentListItem;

public class ListViewAdapter_forParts extends BaseAdapter {
	Context context;
	LayoutInflater listContainter;
	private List<ComponentItem> allItems = new LinkedList<ComponentItem>();

	public ListViewAdapter_forParts(Context context, List<ComponentItem> allParts) {
		this.context = context;
		this.listContainter = LayoutInflater.from(context);

		this.allItems = allParts;
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

		ComponentItem cur = allItems.get(arg0);

		arg1 = new UCtrl_LegoComponentListItem(context, null);
		((UCtrl_LegoComponentListItem) arg1).setValue(cur);

		return arg1;
	}
}
