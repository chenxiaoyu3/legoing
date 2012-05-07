package com.Legoing;

import java.util.List;

import com.Legoing.UserControls.UCtrl_ColorPickerItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ListViewAdapter_forColorItem extends BaseAdapter{

	List<Integer> colorsIndex;
	Context context;
	
	public ListViewAdapter_forColorItem(Context context, List<Integer> colorsIndex)
	{
		this.context = context;
		this.colorsIndex = colorsIndex;
	}
	
	@Override
	public int getCount() {
		// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午3:04:49
		return colorsIndex.size();
	}

	@Override
	/**
	 * return the colorIndex
	 */
	public Object getItem(int arg0) {
		// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午3:04:49
		return colorsIndex.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午3:04:49
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午3:04:49
//		if(arg1 == null)
//		{
//			arg1 = new UCtrl_ColorPickerItem(context, colorsIndex.get(arg0));
//			arg1.setTag(arg1);
//		}else
//		{
//			arg1 = (UCtrl_ColorPickerItem)arg1.getTag();
//		}
		int cor = colorsIndex.get(arg0);
		arg1 = new UCtrl_ColorPickerItem(context, cor);
		if (cor == 0) {
			arg1.setEnabled(false);
		}
		
		return arg1;
	}

}
