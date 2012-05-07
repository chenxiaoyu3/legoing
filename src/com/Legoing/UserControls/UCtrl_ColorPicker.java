package com.Legoing.UserControls;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.params.CoreConnectionPNames;

import com.Legoing.ListViewAdapter_forColorItem;
import com.Legoing.R;
import com.Legoing.StaticOverall;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class UCtrl_ColorPicker extends  LinearLayout{

	
	ListView listView_left, listView_right;
	ListViewAdapter_forColorItem adapter_left, adapter_right;
	AlertDialog outerDialog;
	Handler outerHandler;
	public void setOuterDialog(AlertDialog outerDialog) {
		this.outerDialog = outerDialog;
	}

	public static int selectedColor = 1;
	public UCtrl_ColorPicker(Context context,Handler outerHandler) {
		super(context);
		// TODO , Cxy, 2012-2-15 下午3:02:51
		LayoutInflater.from(context).inflate(R.layout.color_picker, this);
		this.outerHandler = outerHandler;
		initialID();
		
		//initial two color list
		List<Integer> colors_left = new ArrayList<Integer>();
		List<Integer> colors_right = new ArrayList<Integer>();
		int i = 0;
		for(; i < StaticOverall.legoColors.size() / 2; i++) 
			colors_left.add(i);
		for(; i <StaticOverall.legoColors.size(); i++)
			colors_right.add(i);
		
		adapter_left = new ListViewAdapter_forColorItem(context, colors_left);
		listView_left.setAdapter(adapter_left);
		adapter_right = new ListViewAdapter_forColorItem(context, colors_right);
		listView_right.setAdapter(adapter_right);
		
		//set listener
		listView_left.setOnItemClickListener(onItemClickListener);
		listView_right.setOnItemClickListener(onItemClickListener);
		
	}
	AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO , Chen Xiaoyu Cxy, 2012-2-15 下午4:25:36
			Integer colorIndex = (Integer)arg0.getItemAtPosition(arg2);
			selectedColor = colorIndex;
			UCtrl_ColorPicker.this.outerHandler.sendEmptyMessage(1);
			outerDialog.dismiss();
		}
	};
	void initialID()
	{
		this.listView_left = (ListView)findViewById(R.id.listView_colorPicker_left);
		this.listView_right = (ListView)findViewById(R.id.listView_colorPicker_right);
	}
	
	

}
