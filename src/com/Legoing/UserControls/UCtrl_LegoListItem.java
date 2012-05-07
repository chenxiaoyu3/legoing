package com.Legoing.UserControls;

import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ThreadsMgr;
import com.Legoing.ItemDef.LegoItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

public class UCtrl_LegoListItem extends TableLayout {

	Context context;
	LegoItem value;

	static final int MSG_PIC_DOWNLOADED = 1;
	Handler handlerUI;
	ImageView imageView;
	TextView textView_ItemID, textView_ItemName, textView_Year, textView_Cata;
	ProgressBar progressBar_PicLoading;


	public UCtrl_LegoListItem(Context context)
	{
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
		initialID();
		initial();
	}
	public UCtrl_LegoListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
		initialID();
		initial();
	}

	void initial()
	{
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
	}
	void initialID() {
		imageView = (ImageView) findViewById(R.id.IB_ListItem);
		textView_ItemID = (TextView) findViewById(R.id.TV_ListItem_ItemID);
		textView_ItemName = (TextView) findViewById(R.id.TV_ListItem_ItemName);
		textView_Year = (TextView) findViewById(R.id.TV_ListItem_Year);
		textView_Cata = (TextView) findViewById(R.id.TV_ListItem_Catalog);
		progressBar_PicLoading = (ProgressBar) findViewById(R.id.progressBar_ListItem_PicLoading);
	}

	View.OnClickListener imageClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:56:13
			//loadPic();
		}
	};

	public LegoItem getValue() {
		return value;
	}

	public void setValue(LegoItem value) {
		this.value = value;
		reloadValue();
		loadPic();
	}

	public void reloadValue() {
		textView_ItemID.setText(value.getNo());
		textView_ItemName.setText(value.getName());
		textView_Cata.setText(value.getCategory());
		textView_Year.setText(value.getString_Year());
	}

	Thread threadImage = null;

	public void loadPic() {
		progressBar_PicLoading.setVisibility(View.VISIBLE);
		if (threadImage == null) {
			threadImage = ThreadsMgr.getThreadToRun(new Runnable() {

				@Override
				public void run() {
					// TODO , Chen Xiaoyu Cxy, 2011-12-29 ����8:53:19
					Bitmap bit = StaticOverall.imageMagager.getNormalImage(value);

					Message msg = new Message();
					msg.what = MSG_PIC_DOWNLOADED;
					msg.obj = bit;
					handlerUI.sendMessage(msg);
					threadImage = null;

				}
			}, "loadPic:" + value.getNo());
			threadImage.start();
		}

	}

}
