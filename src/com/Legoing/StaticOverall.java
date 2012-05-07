package com.Legoing;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.Legoing.ItemDef.LegoColors;
import com.Legoing.ItemDef.LegoingUser;
import com.Legoing.NetOperation.NetOperation;
import com.Legoing.UserControls.Layout_MyLegoing;

public class StaticOverall {
	
	private static Activity mainActivity;
	public static Layout_MyLegoing layout_MyLegoing;
	public static NetOperation netOperation;
	public static ImageMagager imageMagager;
	public static LegoColors legoColors;
	
	public static LegoingUser curUser = null;
	//public static ProgressBar netStateProgressBar;
	public static boolean DebugLocal = false;
	
	private static Resources resources;

	
	private static Handler handlerOverall;
	public static final int MSG_THREAD_ABORT = 0x00000001;
	
	private static Map<String, Object> globalStorage;

	public static void inital()
	{
	    
		netOperation = new NetOperation();
		imageMagager = new ImageMagager(netOperation);
		globalStorage = new HashMap<String, Object>();
		
	}
	public static void pushGlobalData(String key,Object value)
	{
	    globalStorage.put(key, value);
	}
	public static Object fetchGlobalData(String key)
	{
	    return globalStorage.remove(key);
	}
	
	public static Activity getMainActivity() {
		return mainActivity;
	}
	public static void setMainActivity(Activity mainActivity) {
		StaticOverall.mainActivity = mainActivity;
		resources = mainActivity.getResources();
		legoColors = new LegoColors(resources);
		handlerOverall = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO , Chen Xiaoyu Cxy, 2012-2-18 下午3:19:31
				String str = "";
				switch (msg.what) {
				case MSG_THREAD_ABORT:
					str = resources.getString(R.string.msg_ThreadAbort);
					str += msg.obj.toString();
					break;

				default:
					break;
				}
				Context context = ((Activity_MainTab)StaticOverall.mainActivity).getMainTabHostContext();
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				super.handleMessage(msg);
			}
		};
	}
	public static Resources getResources() {
		return resources;
	}
	public static Handler getHandlerOverall() {
		return handlerOverall;
	}
	public static void shutdown(){
	    System.exit(0);
	}

}
