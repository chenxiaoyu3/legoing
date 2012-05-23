package com.Legoing.UserControls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Legoing.Activity_Base;
import com.Legoing.Activity_FindMiss;
import com.Legoing.Activity_LegoItemInfo;
import com.Legoing.Activity_MainTab;
import com.Legoing.DefaultParamObj;
import com.Legoing.ListViewAdapter_forLegoItem_Groups;
import com.Legoing.ParamObj;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ThreadsMgr;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.NetOperation.NetOperation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

@SuppressWarnings("rawtypes")
public class Layout_Search extends TableLayout {

	Context context;
	Spinner spinner_Types;
	Spinner spinner_Years;
	EditText editText_SearchName, editText_SearchItemNo;
	ImageButton imageButton_Search;
	View view_DoSearch, view_SearchResult;
	ProgressBar progressBar_Loading;

	Resources resources;

	static final int MSG_SEARCH_FINISHED = 1;
	static final int MSG_PIC_DOWNLOADED = 2;
	Handler handlerUI;

	static final int PAGE_SEARCH = 0x1001;
	static final int PAGE_RESULTS = 0x1002;

	int curPage = PAGE_SEARCH;

	public Layout_Search(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.search, this, true);

		initial_ID();
		initial();
		initialTab_Search();
	}

	void initial_ID() {
		spinner_Types = (Spinner) findViewById(R.id.Spinner_Search_Types);
		spinner_Years = (Spinner) findViewById(R.id.Spinner_Search_Years);
		editText_SearchItemNo = (EditText) findViewById(R.id.editText_Search_ItemNo);
		editText_SearchItemNo.setText("8257");
		editText_SearchName = (EditText) findViewById(R.id.editText_Search_Name);
		imageButton_Search = (ImageButton) findViewById(R.id.imageButton_Search_DoSearch);
		view_DoSearch = findViewById(R.id.layout_DoSearch);
		view_SearchResult = findViewById(R.id.layout_SearchResult);
		listView_SearchResult = (ListView) findViewById(R.id.LV_SearchResults);
		progressBar_Loading = (ProgressBar) findViewById(R.id.progressBar_Search_loading);
	}

	void initial() {
		// Type
		String[] typeStrings = getResources().getStringArray(
				R.array.legoItem_types);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, typeStrings);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_Types.setAdapter(adapter);
		spinner_Types.setVisibility(View.VISIBLE);
		view_SearchResult.setVisibility(View.INVISIBLE);
		// Year
		String[] yearStrings = new String[30];
		Date curDate = new Date();
		int curYear = curDate.getYear() + 1900;

		for (int i = 1; i < 30; i++) {
			yearStrings[i] = String.valueOf(curYear + 1 - i);
		}
		yearStrings[0] = "Any";

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, yearStrings);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_Years.setAdapter(adapter2);
		spinner_Years.setVisibility(View.VISIBLE);
		progressBar_Loading.setVisibility(View.INVISIBLE);
		resources = getResources();
		handlerUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// _TODO , Chen Xiaoyu Cxy, 2011-12-16  5:08:27
				switch (msg.what) {
				case MSG_SEARCH_FINISHED:
					afterSearch();
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	ListView listView_SearchResult;
	Button testButton;
	List<List> serverRet;
	List<LegoItem> listItems_SearchResult_Sets,
			listItems_SearchResult_Minifigs, listItems_SearchResult_Parts;
	ListViewAdapter_forLegoItem_Groups listViewAdapter_SearchResult;

	OnClickListener searchOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// _TODO , Cxy, 2011-10-30  9:09:53
			doSearch();
		}
	};

	void jumpPage(int page) {
		switch (page) {
		case PAGE_SEARCH:
			view_DoSearch.setVisibility(View.VISIBLE);
			view_SearchResult.setVisibility(View.INVISIBLE);
			curPage = PAGE_SEARCH;
			break;
		case PAGE_RESULTS:
			view_DoSearch.setVisibility(View.INVISIBLE);
			view_SearchResult.setVisibility(View.VISIBLE);
			curPage = PAGE_RESULTS;
			break;
		default:
			break;
		}
	}

	void initialTab_Search() {

		imageButton_Search.setOnClickListener(searchOnClickListener);
	}
	public void setSearchResult(List<List> result)
	{
	    this.serverRet = result;
	    showSearchResult();
        jumpPage(PAGE_RESULTS);
	}

	void afterSearch() {
		progressBar_Loading.setVisibility(View.INVISIBLE);
		imageButton_Search.setVisibility(View.VISIBLE);
		String msg = "";

		if (searchRet == 0) {
			serverRet = searchParam.value;
			showSearchResult();
			jumpPage(PAGE_RESULTS);
		} else {
			switch (searchRet) {
			case NetOperation.CODE_NET_FAILED:
				msg = resources.getString(R.string.msg_Error_ServerConnectFail);
				break;
			case NetOperation.CODE_INNER_ERROR:
				msg = resources.getString(R.string.msg_Error_INNER);
				break;
			default:
				msg = resources.getString(R.string.msg_Error_Unkown)
						+ String.valueOf(searchRet);
				break;
			}
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	@SuppressWarnings("unchecked")
	void showSearchResult() {
		listItems_SearchResult_Sets = serverRet.get(0);
		listItems_SearchResult_Minifigs = serverRet.get(1);
		listItems_SearchResult_Parts = serverRet.get(2);

		listViewAdapter_SearchResult = new ListViewAdapter_forLegoItem_Groups(
				this.context, listItems_SearchResult_Sets,
				listItems_SearchResult_Minifigs, listItems_SearchResult_Parts);
		listView_SearchResult.setAdapter(listViewAdapter_SearchResult);
		listView_SearchResult
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						LegoItem cur = (LegoItem) arg0.getItemAtPosition(arg2);
						String timestamp = String.valueOf( new Date().getTime() );
	                    Intent i = new Intent(context,Activity_LegoItemInfo.class);
	                    i.putExtra(Activity_LegoItemInfo.EXTRA_GOLBALKEY, timestamp);
	                    StaticOverall.pushGlobalData(timestamp, cur);
	                    context.startActivity(i);
					}
				});
		listView_SearchResult.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 7:43:58 PM
				curSelItem = (LegoItem) parent.getItemAtPosition(position);
				String findMisStr = context.getString(R.string.alertMenu_FindMiss);
				String[] menuStrs = new String[]{findMisStr};
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(menuStrs, menuOnClickListener);
				builder.show();
				return false;
			}
		});
	}
	LegoItem curSelItem;
	DialogInterface.OnClickListener menuOnClickListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 7:51:38 PM
			switch (which) {
			case 0:
				((Activity_Base)context).startFindMissActivity(curSelItem);
				break;

			default:
				break;
			}
		}
	};

	List<LegoItem> getListItems() {
		List<LegoItem> listItems = new ArrayList<LegoItem>();
		LegoItem item = LegoItem.getTestItem();
		item.setNo(":0");
		listItems.add(item);
		item = LegoItem.getTestItem();
		item.setNo(":1");
		listItems.add(item);
		return listItems;
	}

	String type;
	int year;
	String itemNo;
	String name;
	ParamObj<List<List>> searchParam;
	int searchRet;

	void doSearch() {
		type = (String) spinner_Types.getSelectedItem();
		String yearStr = ((String) spinner_Years.getSelectedItem());
		year = 0;
		if (!yearStr.equals("Any")) {
			year = Integer.valueOf(yearStr);
		}
		itemNo = editText_SearchItemNo.getText().toString();
		name = editText_SearchName.getText().toString();
		progressBar_Loading.setVisibility(View.VISIBLE);
		imageButton_Search.setVisibility(View.INVISIBLE);

		Thread thread = ThreadsMgr.getThreadToRun(new Runnable() {

			@Override
			public void run() {
				searchParam = new DefaultParamObj<List<List>>();
				searchRet = StaticOverall.netOperation.getSearchResult(type,
						year, itemNo, name, searchParam);
				handlerUI.sendEmptyMessage(1);
			}
		}, "DO Search");
		thread.start();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		//比不要调用，否则edittext收不到 del 键
	    super.dispatchKeyEvent(event);
	    if (curPage == PAGE_RESULTS) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (curPage == PAGE_RESULTS) {
					jumpPage(PAGE_SEARCH);
					return true;
				}
			}
		}
		return false;

	}

}
