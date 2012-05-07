package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ThreadsMgr;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.Part;
import com.Legoing.ItemDef.UserItem;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

public class UCtrl_CataListItem extends LinearLayout {

	Context context;
	UserItem value;

	static final int MSG_PIC_DOWNLOADED = 1;
	Handler handlerUI;
	ImageView imageView;
	TextView textView_ItemID, textView_ItemName, textView_Year, textView_Color/*, textView_Cata*/;
	TextView textView_Quantity;
	ProgressBar progressBar_PicLoading;
	CheckBox checkBox;

	public UCtrl_CataListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO , Cxy, 2011-12-29 ����8:34:55
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.list_item_catas, this, true);

		initialID();

		imageView.setOnClickListener(imageClickedListener);

		imageView.setVisibility(View.INVISIBLE);
		progressBar_PicLoading.setVisibility(View.INVISIBLE);
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
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 1:43:24 PM
				value.setChecked(isChecked);
				
			}
		});
	}

	void initialID() {
		imageView = (ImageView) findViewById(R.id.imageView_listItemCatas);
		textView_ItemID = (TextView) findViewById(R.id.TV_listItemCatas_ItemID);
		textView_ItemName = (TextView) findViewById(R.id.TV_listItemCatas_ItemName);
		textView_Year = (TextView) findViewById(R.id.TV_listItemCatas_Year);
		textView_Color = (TextView) findViewById(R.id.textView_listItemCatas_Color);
		//textView_Cata = (TextView) findViewById(R.id.TV_listItemCatas_Catalog);
		textView_Quantity = (TextView) findViewById(R.id.textView_listItemCatas_Quantity);
		progressBar_PicLoading = (ProgressBar) findViewById(R.id.progressBar_listItemCatas_PicLoading);
		checkBox = (CheckBox) findViewById(R.id.checkBox_listItemCatas);
	}

	View.OnClickListener imageClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:56:13
			//loadPic();
		}
	};

	public UserItem getValue() {
		return value;
	}

	public void setValue(UserItem value) {
		this.value = value;
		reloadValue();
		loadPic();
	}

	public void reloadValue() {
		LegoItem item = value.getItemInfo();
		if (item != null) {
			textView_ItemID.setText(item.getNo());
			textView_ItemName.setText(item.getName());
			//textView_Cata.setText(item.getCategory());
			textView_Year.setText(item.getString_Year());
			textView_Quantity.setText(String.valueOf(value.getQuantity()));
			if (item.getTYPE() == LegoItem.ITEM_TYPE_PART) {
				int colo = StaticOverall.legoColors.getValue(value.getColorIndex(),true);
				textView_Color.setBackgroundColor(colo);
				textView_Color.setVisibility(View.VISIBLE);
			}else {
				textView_Color.setVisibility(View.INVISIBLE);
			}
			
			if (item.getTYPE() == LegoItem.ITEM_TYPE_SET) {
				checkBox.setVisibility(View.VISIBLE);
			}else {
				checkBox.setVisibility(View.INVISIBLE);
			}
			if (value.isChecked()) {
				checkBox.setChecked(true);
			}else {
				checkBox.setChecked(false);
			}
		}
		
	}

	Thread threadImage = null;

	public void loadPic() {
		progressBar_PicLoading.setVisibility(View.VISIBLE);
		if (threadImage == null && value.getItemInfo() != null) {
			threadImage = ThreadsMgr.getThreadToRun(new Runnable() {

				@Override
				public void run() {
					// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:53:19
					Bitmap bit = StaticOverall.imageMagager.getNormalImage(value.getItemInfo());

					Message msg = new Message();
					msg.what = MSG_PIC_DOWNLOADED;
					msg.obj = bit;
					handlerUI.sendMessage(msg);
					threadImage = null;

				}
			}, "loadPic:" + value.getItemInfo().getNo());
			threadImage.start();
		}

	}
	
	public boolean isChecked()
	{
		return checkBox.isChecked();
	}

}
