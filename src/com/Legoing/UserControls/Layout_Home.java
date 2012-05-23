package com.Legoing.UserControls;

import com.Legoing.Activity_MainTab;
import com.Legoing.DefaultParamObj;
import com.Legoing.ParamObj;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.Set;
import com.Legoing.webimage.ImageRequest;
import com.Legoing.webimage.WebImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Layout_Home extends RelativeLayout {

	Context context;
	ProgressBar progressBar;
	ImageView imageView_TopItemImg;
	TextView textView_TopItemName;
	List<UCtrl_LegoListItem> suggestItemsList;
	LegoItem topItem;
	public Layout_Home(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO , Cxy, 2011-10-30  3:38:39
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.home, this, true);
		
		initial_ID();
		initial();

	}

	void initial_ID() {
	    progressBar = (ProgressBar)findViewById(R.id.progressBar_home);
	    imageView_TopItemImg = (ImageView)findViewById(R.id.imageView_home);
	    textView_TopItemName = (TextView)findViewById(R.id.textView_home_topSetName);
	    suggestItemsList = new ArrayList<UCtrl_LegoListItem>(4);
	    UCtrl_LegoListItem layout = (UCtrl_LegoListItem)findViewById(R.id.LegoListItem_home_1);
	    suggestItemsList.add(layout);
	    layout = (UCtrl_LegoListItem)findViewById(R.id.LegoListItem_home_2);
        suggestItemsList.add(layout);
        layout = (UCtrl_LegoListItem)findViewById(R.id.LegoListItem_home_3);
        suggestItemsList.add(layout);
        layout = (UCtrl_LegoListItem)findViewById(R.id.LegoListItem_home_4);
        suggestItemsList.add(layout);
	}

	void initial() {
		// Type
	    new Async_RequestSuggest().execute(new Object());

	}
	void setItems(final List<Set> items)
	{
	    final LegoItem item = items.get(0);
	    textView_TopItemName.setText(item.getName());
	    WebImageLoader.instance(context).requestImage(item.getBigImgLink(), new ImageRequest.Observer() {
            
            @Override
            public void onImageLoaded(String url, Bitmap bitmap) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 7:20:56 PM
                if (item.getBigImgLink().equals(url)) {
                    imageView_TopItemImg.setImageBitmap(bitmap);
                }
            }
            
            @Override
            public void onImageLoadFailed(String url) {
                // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 7:20:56 PM
                
            }
        });
	    suggestItemsList.get(0).setValue(items.get(1));
	    suggestItemsList.get(1).setValue(items.get(2));
	    suggestItemsList.get(2).setValue(items.get(3));
	    suggestItemsList.get(3).setValue(items.get(4));
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    log("layout home dispaterKeyEvent:"+event.getKeyCode());
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			Activity_MainTab.shutdownConfirm(context);
//			return true;
		}
		return false;

	}
	private void log(String text)
	{
	    android.util.Log.d(TAG, text);
	}
	
	class Async_RequestSuggest extends AsyncTask<Object, Object, Integer>{

        @Override
        protected Integer doInBackground(Object... params) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 4:11:43 PM
            ParamObj<List<Set>> paramObj = new DefaultParamObj<List<Set>>();
            int serverRet = StaticOverall.netOperation.requestTopSuggest(paramObj);
            publishProgress(serverRet,paramObj.value);
            return serverRet;
        }
        @Override
        protected void onProgressUpdate(Object... values) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 7:05:01 PM
            switch ((Integer)values[0]) {
                case 0:
                    List<Set> res = (List<Set>)values[1];
                    setItems(res);
                    break;

                default:
                    break;
            }
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 4:18:42 PM
            progressBar.setVisibility(View.INVISIBLE);
            switch (result) {
                case 0:
                    
                    break;

                default:
                    break;
            }
            super.onPostExecute(result);
        }
	    
	}
	public static final String TAG = "Layout_Home";

}
