package com.Legoing.UserControls;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.Legoing.Activity_Base;
import com.Legoing.Activity_LegoItemInfo;
import com.Legoing.Activity_MainTab;
import com.Legoing.DefaultParamObj;
import com.Legoing.ListViewAdapter_forLegoItem;
import com.Legoing.ListViewAdapter_forLegoItem_Groups;
import com.Legoing.ListViewAdapter_forParts;
import com.Legoing.ParamObj;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ThreadsMgr;
import com.Legoing.TreeViewAdapter_Cata;
import com.Legoing.ItemDef.ComponentItem;
import com.Legoing.ItemDef.ComponentList;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.LegoingUser;
import com.Legoing.ItemDef.Set;
import com.Legoing.ItemDef.UserItem;
import com.Legoing.ItemDef.UserItems;
import com.Legoing.Model.UserSignStateChangedListener;
import com.Legoing.NetOperation.NetOperation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Layout_MyLegoing extends FrameLayout implements TabHost.OnTabChangeListener, UserSignStateChangedListener{

	Context context;

	EditText editText_UName;
	EditText editText_Psd;
	Button button_DoLogin;
	View view_Login, view_UserInfo;
	View view_spiltsParts, view_userItems;
	TextView textView_register;
	TextView textView_selected, textView_succLoad, textView_allPartsCnt;
	// View view_Account;
	ProgressBar progressBar_LoadingUserItems;
	ProgressBar progressBar_Loging;
	ListView listView_fromSetsItems;
	ExpandableListView expandableListView_Items;
	// TextView textView_Account;

	RadioGroup radioGroup_Types;
	RadioButton radioButton_Set, radioButton_Minifig, radioButton_Part, radioButton_fromSets;

	// ListViewAdapter_forLegoItem listViewAdapter_mySets;
	//ListViewAdapter_forLegoItem_Groups listViewAdapter_forLegoItem_Groups_userItems;
	TreeViewAdapter_Cata treeViewAdapter_Cata_Sets, treeViewAdapter_Cata_Minifigs, treeViewAdapter_Cata_Parts;
	// TreeViewAdapter_Cata treeViewAdapter_Cata_fromSets;
	ListViewAdapter_forParts listViewAdapter_forParts;
	// List<LegoItem> listItems_mySets;
	public Handler handlerUI;
	static final int MSG_LOGIN_SUCCESS = 0;
	static final int MSG_LOGIN_FAILED = -1;
	static final int MSG_LOGIN_NET_DISCONNECTED = -10;
	static final int MSG_LOAD_USERITEM_SUCCESS = 10;
	static final int MSG_LOAD_SPILTS_PARTS_SUCCESS = 11;
	static final int MSG_NOTIFY_SIGNIN = 12;
	static final int MSG_INNER_ERROR = -100;

	public static final int MSG_REFRESH_USERSITEMS = 20;

	public static final int PAGE_LOGIN = 0x1001;
	public static final int PAGE_USERINFO = 0x1002;
	int curPage = PAGE_LOGIN;

	public Layout_MyLegoing(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.my_legoing, this, true);
		this.context = context;
		StaticOverall.layout_MyLegoing = this;
		initial_ID();
		initial();
		((Activity_Base)context).addUserSignChangedListener(this);
	}

	View.OnClickListener doLoginListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			doLogin();

		}
	};
	
	AlertDialog alertDialog_LogOff;
	View.OnClickListener doLogOffListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(context);
			aDialogBuilder.setIcon(R.drawable.lego_1);
			aDialogBuilder.setTitle(R.string.Confirm);
			aDialogBuilder.setMessage(R.string.msg_LogOff);
			aDialogBuilder.setPositiveButton(StaticOverall.getResources().getString(R.string.YES),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// _TODO , Chen Xiaoyu Cxy, 2012-1-3 ����9:00:32
							StaticOverall.curUser = null;
							jumpPage(PAGE_LOGIN);

						}
					});
			aDialogBuilder.setNegativeButton(StaticOverall.getResources().getString(R.string.CANCLE),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// alertDialog_LogOff.dismiss();
						}
					});
			alertDialog_LogOff = aDialogBuilder.create();
			alertDialog_LogOff.show();

		}
	};

	RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// _TODO , Chen Xiaoyu Cxy, 2012-1-5 ����10:35:12
			if (checkedId == radioButton_fromSets.getId()) {
				view_userItems.setVisibility(View.INVISIBLE);
				view_spiltsParts.setVisibility(View.VISIBLE);
				loadSpiltSelectexSets();
			} else {
				view_userItems.setVisibility(View.VISIBLE);
				view_spiltsParts.setVisibility(View.INVISIBLE);
				if (checkedId == radioButton_Set.getId()) {
					expandableListView_Items.setAdapter(treeViewAdapter_Cata_Sets);
				} else if (checkedId == radioButton_Minifig.getId())
					expandableListView_Items.setAdapter(treeViewAdapter_Cata_Minifigs);
				else if (checkedId == radioButton_Part.getId()) {
					expandableListView_Items.setAdapter(treeViewAdapter_Cata_Parts);
				}
			}

		}
	};

	public void jumpPage(int page) {
		switch (page) {
		case PAGE_LOGIN:
			view_Login.setVisibility(View.VISIBLE);
			view_UserInfo.setVisibility(View.INVISIBLE);
			curPage = PAGE_LOGIN;
			break;
		case PAGE_USERINFO:
			view_Login.setVisibility(View.INVISIBLE);
			view_UserInfo.setVisibility(View.VISIBLE);
			curPage = PAGE_USERINFO;
			break;

		default:
			break;
		}
	}

	void initial() {

	    textView_register.setMovementMethod(LinkMovementMethod.getInstance());
		button_DoLogin.setOnClickListener(doLoginListener);
		// view_Account.setOnClickListener(doLogOffListener);
		listView_fromSetsItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// _TODO , Chen Xiaoyu Cxy, 2011-11-8  6:57:24
			    UCtrl_LegoComponentListItem layout = (UCtrl_LegoComponentListItem)arg1;
			    ComponentItem item = layout.getValue();
                ((Activity_Base)context).startLegoItemInfoActivity(item.getItemInfo());

			}
		});

		radioGroup_Types.setOnCheckedChangeListener(onCheckedChangeListener);

		expandableListView_Items.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// _TODO , Chen Xiaoyu Cxy, 2012-1-5  11:12:30
				UserItem curUserItem = ((UCtrl_CataListItem) v).getValue();
				if (curUserItem != null) {
					LegoItem cur = curUserItem.getItemInfo();
					((Activity_Base)context).startLegoItemInfoActivity(cur);
					return true;
				}

				return false;
			}
		});

		handlerUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO , Chen Xiaoyu Cxy, 2011-12-31 ����2:19:23
				switch (msg.what) {
				case MSG_LOGIN_SUCCESS:
					progressBar_Loging.setVisibility(View.INVISIBLE);
					button_DoLogin.setEnabled(true);
					jumpPage(PAGE_USERINFO);
					loadUserItems();
					
					break;
				case MSG_LOGIN_FAILED:
					progressBar_Loging.setVisibility(View.INVISIBLE);
					button_DoLogin.setEnabled(true);
					Toast.makeText(context, StaticOverall.getResources().getString(R.string.msg_WrongUserNameOrPsd),
							Toast.LENGTH_LONG).show();
					clearInput();
					break;
				case MSG_LOGIN_NET_DISCONNECTED:
					progressBar_Loging.setVisibility(View.INVISIBLE);
					button_DoLogin.setEnabled(true);
					Toast.makeText(context,
							StaticOverall.getResources().getString(R.string.msg_Error_ServerConnectFail),
							Toast.LENGTH_LONG).show();
					break;
				case MSG_LOAD_USERITEM_SUCCESS: {
					progressBar_LoadingUserItems.setVisibility(View.INVISIBLE);
					List<UserItem> uu = StaticOverall.curUser.getItems().getPlist().get(0);
					treeViewAdapter_Cata_Sets = TreeViewAdapter_Cata.getAdapterFromItems(context, uu);

					treeViewAdapter_Cata_Minifigs = TreeViewAdapter_Cata.getAdapterFromItems(context,
							StaticOverall.curUser.getItems().getPlist().get(1));

					treeViewAdapter_Cata_Parts = TreeViewAdapter_Cata.getAdapterFromItems(context,
							StaticOverall.curUser.getItems().getPlist().get(2));

					expandableListView_Items.setAdapter(treeViewAdapter_Cata_Sets);
					radioButton_Set.setChecked(true);

					break;
				}
				case MSG_REFRESH_USERSITEMS:
					progressBar_LoadingUserItems.setVisibility(View.VISIBLE);
					loadUserItems();
					break;
				case MSG_LOAD_SPILTS_PARTS_SUCCESS:
					progressBar_LoadingUserItems.setVisibility(View.INVISIBLE);
					listViewAdapter_forParts = new ListViewAdapter_forParts(context, (List<ComponentItem>) msg.obj);
					textView_selected.setText(String.valueOf(msg.arg1));
					textView_succLoad.setText(String.valueOf(msg.arg2));
					listView_fromSetsItems.setAdapter(listViewAdapter_forParts);
					break;
				case MSG_NOTIFY_SIGNIN:
				    ((Activity_Base)context).notifySign(true);
				    break;

				default:
					Toast.makeText(context, StaticOverall.getResources().getString(R.string.msg_Error_INNER),
							Toast.LENGTH_LONG).show();
					break;
				}

				super.handleMessage(msg);
			}
		};
		

	}

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

	void initial_ID() {
		editText_Psd = (EditText) findViewById(R.id.editText_lgoin_Psd);
		editText_UName = (EditText) findViewById(R.id.editText_login_UName);

		view_Login = findViewById(R.id.layout_login);
		view_UserInfo = findViewById(R.id.layout_loggedIn);
		view_userItems = findViewById(R.id.linearLayout_userItems);
		view_spiltsParts = findViewById(R.id.linearLayout_fromSets);
		textView_register = (TextView)findViewById(R.id.textView_myLego_register);
		textView_selected = (TextView) findViewById(R.id.textView_myLego_Selected);
		textView_succLoad = (TextView) findViewById(R.id.textView_myLego_loadSuccess);
		textView_allPartsCnt = (TextView) findViewById(R.id.textView_myLego_allPartsCount);
		progressBar_LoadingUserItems = (ProgressBar) findViewById(R.id.progressBar_MyLegoing_loadingMyItems);
		progressBar_Loging = (ProgressBar) findViewById(R.id.progressBar_myLegoing_loging);
		button_DoLogin = (Button) findViewById(R.id.button_myLegoing_DoLogin);
		listView_fromSetsItems = (ListView) findViewById(R.id.listView_MyLegoing_items);
		expandableListView_Items = (ExpandableListView) findViewById(R.id.expandableListView_myLego_Sets);
		radioGroup_Types = (RadioGroup) findViewById(R.id.radioGroup_myLego_Types);
		radioButton_Set = (RadioButton) findViewById(R.id.radioButton_myLego_Set);
		radioButton_Minifig = (RadioButton) findViewById(R.id.radioButton_myLego_Minifig);
		radioButton_Part = (RadioButton) findViewById(R.id.radioButton_myLego_Part);
		radioButton_fromSets = (RadioButton) findViewById(R.id.radioButton_myLego_PartsFromSet);
	}

	String uName, uPsd;

	void doLogin() {
		uName = editText_UName.getText().toString();
		if (uName == null || uName.length() == 0) {
			Toast.makeText(context, StaticOverall.getResources().getString(R.string.msg_EnterUserNamePlease),
					Toast.LENGTH_SHORT).show();
			return;
		}
		uPsd = editText_Psd.getText().toString();
		if (uPsd == null || uPsd.length() == 0) {
			Toast.makeText(context, StaticOverall.getResources().getString(R.string.msg_EnterUserPsdPlease),
					Toast.LENGTH_SHORT).show();
			return;
		}
		progressBar_Loging.setVisibility(View.VISIBLE);
		button_DoLogin.setEnabled(false);
		Thread thread = ThreadsMgr.getThreadToRun(new Runnable() {

			@Override
			public void run() {
				// TODO , Chen Xiaoyu Cxy, 2011-12-31 ����2:04:28
				ParamObj<LegoingUser> retValue = new DefaultParamObj<LegoingUser>();
				int ret = StaticOverall.netOperation.userVerify(uName, uPsd, retValue);
				switch (ret) {
				case 0:
					StaticOverall.curUser = retValue.value;
					handlerUI.sendEmptyMessage(MSG_LOGIN_SUCCESS);
					break;
				case -1:
					handlerUI.sendEmptyMessage(MSG_LOGIN_FAILED);
					break;
				case NetOperation.CODE_NET_FAILED:
					handlerUI.sendEmptyMessage(MSG_LOGIN_NET_DISCONNECTED);
					break;
				default:
					handlerUI.sendEmptyMessage(ret);
					break;
				}
			}
		}, "do login");
		thread.start();

	}

	private void clearInput() {
		editText_Psd.setText("");
		editText_UName.setText("");
	}
	public void needRefreshUserItems()
	{
	    handlerUI.sendEmptyMessage(MSG_REFRESH_USERSITEMS);
	}

	void loadUserItems() {
		Thread thread = ThreadsMgr.getThreadToRun(new Runnable() {

			@Override
			public void run() {
				// TODO , Chen Xiaoyu Cxy, 2012-1-5 ����1:14:13
				ParamObj<UserItems> paramObj = new DefaultParamObj<UserItems>();
				int ret = StaticOverall.netOperation.getUserItems(StaticOverall.curUser.getUserIndex(), paramObj);

				switch (ret) {
				case 0:
					StaticOverall.curUser.setItems(paramObj.value);
					// value.setComponentList(paramObj.value);
					// for (List<UserItem> li0 :
					// StaticOverall.curUser.getItems()
					// .getPlist()) {
					// for (UserItem userItem : li0) {
					// ParamObj<LegoItem> retValueObj = new
					// DefaultParamObj<LegoItem>();
					// int retInfo = StaticOverall.netOperation.getItemInfo(
					// userItem.getType(), userItem.getItemNo(),
					// retValueObj);
					// if (retInfo == 0) {
					// userItem.setItemInfo(retValueObj.value);
					// }
					// }
					// }
					//handlerUI.sendEmptyMessage(MSG_LOAD_USERITEM_SUCCESS);
					//给Activity_Base发送广播，自己也会收到登陆通知，自己就不需要发给自己了
					handlerUI.sendEmptyMessage(MSG_NOTIFY_SIGNIN);
					break;
				case NetOperation.CODE_INNER_ERROR:
					handlerUI.sendEmptyMessage(MSG_INNER_ERROR);
				default:
					// handlerUI.sendEmptyMessage(MSG_COMPONENTLIST_LOAD_UNKOWN_RET);
					break;
				}
			}
		}, "load user items");
		thread.start();

	}

	void loadSpiltSelectexSets() {
		progressBar_LoadingUserItems.setVisibility(View.VISIBLE);
		Thread thread = ThreadsMgr.getThreadToRun(new Runnable() {

			@Override
			public void run() {
				// TODO , Chen Xiaoyu Cxy, 2012-2-18 下午4:13:58
				List<ComponentItem> retItems = new LinkedList<ComponentItem>();

				List<UserItem> allSets = StaticOverall.curUser.getItems().getPlist().get(0);
				int succ = 0, all = 0;
				for (UserItem userItem : allSets) {
					if (!userItem.isChecked()) {
						continue;
					}
					all++;
					Set each = (Set) userItem.getItemInfo();
					ParamObj<ComponentList> paramObj = new DefaultParamObj<ComponentList>();
					if (each.getComponentList() == null) {
						int ret = StaticOverall.netOperation.getComponentList(each.getNo(), each.getType(), paramObj);
						switch (ret) {
						case 0:
							ComponentList componentList = paramObj.value;
							each.setComponentList(componentList);							
							
							break;
						case NetOperation.CODE_NET_FAILED:
							// handlerUI.sendEmptyMessage(MSG_NET_FAILED);
							break;
						case -1:
							// handlerUI.sendEmptyMessage(MSG_COMPONENTLIST_LOAD_NOT_FOUND);
							break;
						default:
							// handlerUI.sendEmptyMessage(MSG_COMPONENTLIST_LOAD_UNKOWN_RET);
							break;
						}
					}
					if (each.getComponentList() != null) {
						succ++;
						//2012/2/18 cxy: will calculate quantity repeatly, so clone
						List<ComponentItem> allParts = each.getComponentList().getAllItems(LegoItem.ITEM_TYPE_PART);
						List<ComponentItem> allClone = new ArrayList<ComponentItem>(allParts.size());
						for (ComponentItem componentItem : allParts) {
							allClone.add((ComponentItem)componentItem.clone());
						}
						
						for (ComponentItem componentItem : allClone) {
							componentItem.setQuantity(componentItem.getQuantity() * userItem.getQuantity());
						}
						retItems.addAll(allClone);
					}
				}
				Message msg = new Message();
				msg.what = MSG_LOAD_SPILTS_PARTS_SUCCESS;
				msg.arg1 = all;
				msg.arg2 = succ;
				msg.obj = retItems;
				handlerUI.sendMessage(msg);
			}
		}, "loadSpiltSelectexSets");
		thread.start();

	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    Log.d("Layout_MyLegoing", "dispater" + event.getKeyCode());
////		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
////			Activity_MainTab.shutdownConfirm(context);
////			return false;
////		} else {
////			return super.dispatchKeyEvent(event);
////		}
//	    return sup;
//
//	}
	
	
	class Requet_Signin extends AsyncTask<String, Object, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:15:36 PM
            int ret = ((Activity_Base)context).doSignIn(params[0], params[1]);
            return ret;
        }
        @Override
        protected void onPostExecute(Integer result) {
            // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:20:32 PM
            if (result == 0) {
                
            }else if(result == -1){
                button_DoLogin.setEnabled(true);
                Toast.makeText(context, StaticOverall.getResources().getString(R.string.msg_WrongUserNameOrPsd),
                        Toast.LENGTH_LONG).show();
                clearInput();
            }else {
                StaticOverall.netOperation.handleReturnValue(result);
            }
            super.onPostExecute(result);
        }
	    
	}

    @Override
    public void onTabChanged(String tabId) {
        // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 4:27:31 PM
        if (tabId.equals(Activity_MainTab.TAB_MYLEGO)) {
            if (StaticOverall.curUser == null) {
                jumpPage(Layout_MyLegoing.PAGE_LOGIN);
            }else {
                handlerUI.sendEmptyMessage(MSG_LOGIN_SUCCESS);
                handlerUI.sendEmptyMessage(MSG_LOAD_USERITEM_SUCCESS);
            }
        }
    }

    @Override
    public void onUserSignStateChanged(boolean signed) {
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 5:14:09 PM
        
        if (!signed) {
            jumpPage(Layout_MyLegoing.PAGE_LOGIN);
        }else {
            //if (curPage == PAGE_LOGIN) {
                progressBar_Loging.setVisibility(View.INVISIBLE);
                jumpPage(PAGE_USERINFO);
                handlerUI.sendEmptyMessage(MSG_LOAD_USERITEM_SUCCESS);
            //}
           
        }
    }

}
