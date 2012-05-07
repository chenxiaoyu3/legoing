package com.Legoing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.ComponentItemCompare;
import com.Legoing.ItemDef.ComponentList;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.Set;
import com.Legoing.ItemDef.UserItem;
import com.Legoing.NetOperation.NetOperation;
import com.Legoing.UserControls.ActionBar;
import com.Legoing.UserControls.Layout_FindMissListItem;
import com.Legoing.UserControls.Layout_ItemSelection;
import com.Legoing.UserControls.PopupWindow_MultiSelect;
import com.Legoing.UserControls.UCtrl_CataListItem;
import com.Legoing.UserControls.UCtrl_LegoListItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Activity_FindMiss extends Activity_Base{
	
    private ActionBar mActionBar;
	private LegoItem mCurItem;
	private List<ComponentItem> mAllComponents;
	private List<ComponentItemCompare> mCompareList;
	private int mSelCnt, mSuccCnt;
	private boolean mIgnoreColor = false;
	
	private UCtrl_LegoListItem mViewCurItem;
	private View mViewShowSelectWin;
	private ProgressBar mProgressBar;
	private ListView mListView;
	private TextView mTextView_SelCnt;
	private ImageButton mImageButton_Setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 7:26:18 PM
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_miss);
		String key = getIntent().getStringExtra(EXTRA_GOLBALKEY);
		mCurItem = (LegoItem)StaticOverall.fetchGlobalData(key);
		initialIDs();
		mViewCurItem.setValue(mCurItem);
		mViewShowSelectWin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 11:53:23 AM
				if (!isSignedIn()) {
                    mActionBar.doSignInOrOff();
                    return;
                }
				PopupWindow_MultiSelect win = new PopupWindow_MultiSelect(Activity_FindMiss.this);
				win.setValue(StaticOverall.curUser.getItems().getPlist().get(0));
				win.setPositiveButton(R.string.Confirm, new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 11, 2012 9:16:43 PM
						new Async_DoFindMiss().execute(new Object[]{});
					}
				});
				win.setNegtiveButton(R.string.CANCLE, null);
				win.show();
				
			}
		});
		mImageButton_Setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 3:01:38 PM
				View view = LayoutInflater.from(Activity_FindMiss.this).inflate(R.layout.find_miss_setting, null);
				final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox_findMissSetting_ignoreColor);
				checkBox.setSelected(mIgnoreColor);
				new AlertDialog.Builder(Activity_FindMiss.this)
				.setView(view)
				.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 3:06:20 PM
						mIgnoreColor = checkBox.isChecked();
					}
				})
				.show();
				
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 25, 2012 4:09:19 PM
                Layout_FindMissListItem layout = (Layout_FindMissListItem)view;
                Activity_FindMiss.this.startLegoItemInfoActivity(layout.getItemInfo());
            }
        });
	}
	
	private void initialIDs()
	{
	    this.mActionBar = (ActionBar)findViewById(R.id.actionBar_findMiss);
		this.mViewCurItem  = (UCtrl_LegoListItem)findViewById(R.id.uCtrl_LegoListItem);
		this.mViewShowSelectWin = findViewById(R.id.layout_findMiss_showSelectWin);
		this.mProgressBar = (ProgressBar)findViewById(R.id.progressBar_findMiss);
		this.mListView = (ListView)findViewById(R.id.listView_findMiss);
		this.mImageButton_Setting = (ImageButton)findViewById(R.id.imageButton_findMiss_setting);
		this.mTextView_SelCnt = (TextView)findViewById(R.id.textView_findMiss_selCnt);
	}
	
	
	private int loadSelectionComponents()
	{
		ParamObj<ComponentList> sel = new DefaultParamObj<ComponentList>();
		int ret = StaticOverall.netOperation.getComponentList(mCurItem.getNo(), mCurItem.getType(), sel);
		if (ret == 0) {
			mCurItem.setComponentList(sel.value);
		}else {
			return -1;
		}
		
		mAllComponents = new LinkedList<ComponentItem>();
		
		List<UserItem> allSets = StaticOverall.curUser.getItems().getPlist().get(0);
		for (UserItem userItem : allSets) {
			if (!userItem.isChecked()) {
				continue;
			}
			mSelCnt++;
			Set each = (Set) userItem.getItemInfo();
			ParamObj<ComponentList> paramObj = new DefaultParamObj<ComponentList>();
			if (each.getComponentList() == null) {
				ret = StaticOverall.netOperation.getComponentList(each.getNo(), each.getType(), paramObj);
				switch (ret) {
				case 0:
					ComponentList componentList = paramObj.value;
					each.setComponentList(componentList);							
					
					break;
				case NetOperation.CODE_NET_FAILED:
					break;
				case -1:
					break;
				default:
					break;
				}
			}
			if (each.getComponentList() != null) {
				//2012/2/18 cxy: will calculate quantity repeatly, so clone
				mSuccCnt++;
				List<ComponentItem> allParts = each.getComponentList().getAllItems(LegoItem.ITEM_TYPE_PART);
				List<ComponentItem> allClone = new ArrayList<ComponentItem>(allParts.size());
				
				
				Map<String, ComponentItem> map = new HashMap<String, ComponentItem>();
				for (ComponentItem componentItem : allParts) {
					String no = componentItem.getItemInfo().getNo();
					if (map.containsKey(no)) {
						ComponentItem item = map.get(no);
						item.setQuantity(item.getQuantity() + componentItem.getQuantity()* userItem.getQuantity());
					}else {
						ComponentItem item =(ComponentItem)componentItem.clone();
						item.setQuantity(item.getQuantity() * userItem.getQuantity());
						allClone.add(item);
						map.put(item.getItemInfo().getNo(), item);
					}
					
				}
				
//				for (ComponentItem componentItem : allClone) {
//					componentItem.setQuantity(componentItem.getQuantity() * );
//				}
				mAllComponents.addAll(allClone);
			}
		}
		return 0;
		
		
	}
	private void doFindMiss()
	{
		List<ComponentItem> parts = mCurItem.getComponentList().getAllItemsIgnoreColor(LegoItem.ITEM_TYPE_PART);
		mCompareList = new ArrayList<ComponentItemCompare>(parts.size());
		
		for (ComponentItem compt : parts) {
			ComponentItemCompare nItem = new ComponentItemCompare(compt);
			ComponentItem itemInTotal = search(compt.getItemInfo().getNo(), mAllComponents);
			if (itemInTotal == null) {
				nItem.setQtyTotal(0);
			}else {
				nItem.setQtyTotal(itemInTotal.getQuantity());
			}
			mCompareList.add(nItem);
		}
	}
	private ComponentItem search(String itemNo, List<ComponentItem> all)
	{
		for (ComponentItem item : all) {
			if (item.getItemInfo().getNo().equals(itemNo)) {
				return item;
			}
		}
		return null;
	}
	class Async_DoFindMiss extends AsyncTask<Object, Object, Object>
	{
		@Override
		protected void onPreExecute() {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 9:57:21 AM
			mProgressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 9:57:27 AM
			loadSelectionComponents();
			doFindMiss();
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 9:57:31 AM
			mProgressBar.setVisibility(View.INVISIBLE);
			mTextView_SelCnt.setText(String.valueOf(mSelCnt));
			mListView.setAdapter(new ListViewAdapter_Compare());
			super.onPostExecute(result);
		}
	}
	
	class ListViewAdapter_Compare extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 11:08:37 AM
			return mCompareList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 11:08:37 AM
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 11:08:37 AM
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 12, 2012 11:08:37 AM
			Layout_FindMissListItem layout = null;
			if (convertView == null) {
				layout = new Layout_FindMissListItem(Activity_FindMiss.this);
				layout.setTag(layout);
			}else {
				layout = (Layout_FindMissListItem)convertView.getTag();
			}
			layout.setInfo(mCompareList.get(position));
			
			return layout;
		}
		
	}

	
	//public static final String EXTRA_ITEM = "item";

}
