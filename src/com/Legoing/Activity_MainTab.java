package com.Legoing;

import com.Legoing.NetOperation.NetOperation;
import com.Legoing.UserControls.ActionBar;
import com.Legoing.UserControls.Layout_MyLegoing;
import com.Legoing.UserControls.Layout_Search;
import com.Legoing.zxing.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Activity_MainTab extends Activity_Base {

	TabHost tabHost;
	ActionBar actionBar;
	Layout_MyLegoing mTab_MyLego;
	Layout_Search mTab_Search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO , Cxy, 2011-10-24 ����6:40:16

		super.onCreate(savedInstanceState);
		StaticOverall.setMainActivity(this);
		StaticOverall.inital();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_tab);
		initialID();
		startLoading();
		initalTab();
		testCode();
		
		actionBar.setTitle(getResources().getDrawable(R.drawable.word_legoing));
	}

	void initialID()
	{
		tabHost = (TabHost) findViewById(R.id.tabHost);
		actionBar = (ActionBar)findViewById(R.id.actionBar_mainTab);
		mTab_MyLego = (Layout_MyLegoing)findViewById(R.id.layout_MyLegoing);
		mTab_Search = (Layout_Search)findViewById(R.id.layout_Search);
		//layout_Account = findViewById(R.id.relativeLayout_Account);
		//textView_Account = (TextView)findViewById(R.id.textView_mainTab_Account);
	}
	void testCode() {
		// StaticOverall.netOperation.testSearch();
	}

	void initalTab() {
		
		tabHost.setup();

		View searchTabView = android.view.LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, null);
		((ImageView) searchTabView.findViewById(R.id.imageView_tab_indicator))
				.setImageResource(R.drawable.selector_nav_button);
		tabHost.addTab(tabHost.newTabSpec(TAB_TOPSUGGET)
				.setIndicator(searchTabView)
				// .setIndicator("Home")
				.setContent(R.id.mainTab_Home));
		searchTabView = android.view.LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, null);
		((ImageView) searchTabView.findViewById(R.id.imageView_tab_indicator))
//				.setImageResource(R.drawable.word_search);
		        .setImageResource(R.drawable.selector_nav_search);
		// Layout_TabIndicator searchTabView2 = new Layout_TabIndicator(this);
		// searchTabView2.setWord(R.drawable.word_search);
		tabHost.addTab(tabHost.newTabSpec(TAB_SEARCH)
		// .setIndicator("Search",getResources().getDrawable(R.drawable.main_tab_selected))
				.setIndicator(searchTabView).setContent(R.id.mainTab_Search));
		searchTabView = android.view.LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, null);
		ImageView word_MyLego = (ImageView) searchTabView
				.findViewById(R.id.imageView_tab_indicator);
		word_MyLego.setImageResource(R.drawable.selector_nav_user);
		// word_MyLego.setLayoutParams(new LayoutParams(
		// (int)StaticOverall.getResources().getDimension(R.dimen.tab_word_width),
		// (int)StaticOverall.getResources().getDimension(R.dimen.tab_word_height)));
		tabHost.addTab(tabHost.newTabSpec(TAB_MYLEGO)
				.setIndicator(searchTabView)
				// .setIndicator("My Legoing")
				.setContent(R.id.mainTab_User));
		// searchTabView =
		// android.view.LayoutInflater.from(this).inflate(R.layout.tab_indicator,
		// null);
		// tabHost.addTab(tabHost
		// .newTabSpec("TabSpec_Test")
		// .setIndicator(searchTabView)
		// //.setIndicator("Test")
		// .setContent(R.id.mainTab_Test));

//		tabHost.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO , Chen Xiaoyu Cxy, 2011-11-30 ����4:31:16
//			    log("tabhost listener:"+keyCode);
//				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//				    
//				}
//				return false;
//			}
//
//			DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO , Chen Xiaoyu Cxy, 2011-11-30 ����4:35:37
//				    
//				}
//			};
//		});
		tabHost.setOnTabChangedListener(mTab_MyLego);

	}
	TabHost.OnTabChangeListener mOnTabChangeListener = new TabHost.OnTabChangeListener() {
        
        @Override
        public void onTabChanged(String tabId) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:08:17 PM
            if (tabId.equals(Activity_MainTab.TAB_MYLEGO)) {
                if (StaticOverall.curUser == null) {
                    mTab_MyLego.jumpPage(Layout_MyLegoing.PAGE_LOGIN);
                }else {
                    mTab_MyLego.jumpPage(Layout_MyLegoing.PAGE_USERINFO);
                }
            }
        }
    };


	void showNetState() {
		String str = "";
		Resources resources = getResources();
		switch (StaticOverall.netOperation.netMode) {
		case NetOperation.NET_MODE_NONE:
			str = resources.getString(R.string.msg_netMode_None);
			break;
		case NetOperation.NET_MODE_WIFI:
			str = resources.getString(R.string.msg_netMode_Wifi);
			break;
		case NetOperation.NET_MODE_MOBILE:
			str = resources.getString(R.string.msg_netMode_Mobile);
			break;
		default:
			break;
		}
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
		toast.show();
	}

	public void startLoading() {
		Intent intent = new Intent(this, Activity_Loading.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO , Cxy, 2011-11-25 3:09:40
		
		switch (requestCode) {
            case REQ_BARCODESCAN:
                if (resultCode == RESULT_OK) {
                    String dat = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                    Toast.makeText(this, dat, Toast.LENGTH_LONG).show();
                    new Async_RequestBarcodeSearch().execute(dat);
                }
                break;

            default:
                showNetState();
                break;
        }
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	
	public Context getMainTabHostContext()
	{
		return tabHost.getContext();
	}
	
	static final int MENU_ID_LOADPEERSON = 0;


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// _TODO , Chen Xiaoyu Cxy, 2012-3-1 下午2:46:43
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 1:09:00 PM
		switch (item.getItemId()) {
		case R.id.menuItem_import:
			Intent intent = new Intent(this, Activity_Import.class);
			startActivity(intent);
			break;
		case R.id.menuItem_qrCode:
		    startBarcodeScan();
		    break;
		case R.id.menuItem_exit:
		    super.shutdownConfirm();
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 24, 2012 5:19:53 PM
//	    log("Activity_mainTab dispatcherKeyEvnt:"+event.getKeyCode());
//	    boolean ret =super.dispatchKeyEvent(event); 
//	    if(!ret ){
//	        int a = 0;
//	        a++;
//	    }
//	    return ret;
//	}
	public Layout_MyLegoing getTab_myLego()
	{
	    return mTab_MyLego;
	}
	public Layout_Search getTab_search()
	{
	    return mTab_Search;
	}
	public void jumpTab(int index)
	{
	    tabHost.setCurrentTab(index);
	}
	class Async_RequestBarcodeSearch extends AsyncTask<Object, Object, Object>{

        @Override
        protected Object doInBackground(Object... params) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 7:56:55 PM
            ParamObj<List<List>> paramObj = new DefaultParamObj<List<List>>();
            int serverRet = StaticOverall.netOperation.requestBarcodeSearch((String)params[0], paramObj);
            publishProgress(serverRet, paramObj.value);
            return serverRet;
        }
        @Override
        protected void onProgressUpdate(Object... values) {
            // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 8:09:33 PM
            switch ((Integer)values[0]) {
                case 0:
                    jumpTab(1);
                    mTab_Search.setSearchResult((List<List>)values[1]); 
                    break;
                case -1:
                    Toast.makeText(Activity_MainTab.this, R.string.barcode_searchFailed, Toast.LENGTH_LONG).show();
                    break;
                default:
                    StaticOverall.netOperation.handleReturnValue((Integer)values[0]);
                    break;
            }
            super.onProgressUpdate(values);
        }
	    
	}
	private static void log(String text)
	{
	    android.util.Log.d(TAG, text);
	}
	public static final String TAB_TOPSUGGET = "top_suggest";
	public static final String TAB_SEARCH = "search";
	public static final String TAB_MYLEGO = "my_lego";
	public static final String TAG = "Activity_MainTab";
}
