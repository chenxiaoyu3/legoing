package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ThreadsMgr;
import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.IComponentItem;
import com.Legoing.ItemDef.ILegoItem;
import com.Legoing.ItemDef.LegoItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UCtrl_LegoComponentListItem extends LinearLayout {

	Context context;
	//ILegoItem legoItem;
	ComponentItem componentItem;

	static final int MSG_PIC_DOWNLOADED = 1;
	Handler handlerUI;
	ImageView imageView;
	View layout_Line;
	TextView textView_ItemID, textView_ItemName, textView_Year, textView_Cata, textView_Quantity;
	ProgressBar progressBar_PicLoading;
	
	public static boolean LineBackColor = false;
	boolean curLineBackColor;
	public UCtrl_LegoComponentListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO , Cxy, 2011-12-29 ����8:34:55
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.component_list_item, this, true);
		LineBackColor = !LineBackColor;
		curLineBackColor = LineBackColor;
		initialID();

		//imageView.setOnClickListener(imageClickedListener);

		//imageView.setVisibility(View.INVISIBLE);
		//progressBar_PicLoading.setVisibility(View.INVISIBLE);
		handlerUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO , Chen Xiaoyu Cxy, 2011-12-16 ����5:08:27
				switch (msg.what) {
				case MSG_PIC_DOWNLOADED:
					progressBar_PicLoading.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
					}
					break;
				}
				super.handleMessage(msg);
			}
		};
		if (curLineBackColor) {
			layout_Line.setBackgroundColor(StaticOverall.getResources().getColor(R.color.color_gray_for_listline));
		
		}
	}

	void initialID() {
		layout_Line = findViewById(R.id.linearLayout_ComponentListItem);
		//imageView = (ImageView) findViewById(R.id.imageView_ComponentListItem);
		textView_ItemID = (TextView) findViewById(R.id.TV_ComponentListItem_ItemID);
		textView_ItemName = (TextView) findViewById(R.id.TV_ComponentListItem_ItemName);
		//textView_Year = (TextView) findViewById(R.id.TV_ComponentListItem_Year);
		//textView_Cata = (TextView) findViewById(R.id.TV_ComponentListItem_Catalog);
		textView_Quantity = (TextView) findViewById(R.id.textView_ComponentListItem_Quantitiy);
		//progressBar_PicLoading = (ProgressBar) findViewById(R.id.progressBar_ComponentListItem_PicLoading);
	}

	View.OnClickListener imageClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:56:13
			//loadPic();
		}
	};



	public void setValue(ComponentItem componentItem) {
		//this.legoItem = legoItem;
		this.componentItem = componentItem;
		reloadValue();
		//loadPic();
	}
	public ComponentItem getValue()
	{
	    return this.componentItem;
	}

	public void reloadValue() {
		LegoItem item = componentItem.getItemInfo();
		textView_ItemID.setText(item.getNo());
		textView_Quantity.setText(String.valueOf(componentItem.getQuantity()));
		textView_ItemName.setText(item.getName());
		//textView_Cata.setText(value.getCategory());
		//textView_Year.setText(value.getString_Year());
	}

	Thread threadImage = null;

//	public void loadPic() {
//		progressBar_PicLoading.setVisibility(View.VISIBLE);
//		if (threadImage == null) {
//			threadImage = ThreadsMgr.getThreadToRun(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:53:19
//					Bitmap bit = StaticOverall.imageMagager.getNormalImage(value);
//
//					Message msg = new Message();
//					msg.what = MSG_PIC_DOWNLOADED;
//					msg.obj = bit;
//					handlerUI.sendMessage(msg);
//					threadImage = null;
//
//				}
//			}, "loadPic:" + value.getNo());
//			threadImage.start();
//		}
//
//	}

}
