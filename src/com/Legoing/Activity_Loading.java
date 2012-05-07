package com.Legoing;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;


public class Activity_Loading extends Activity {
	
	TabHost tabHost;
	ImageView iv_legoing,iv_star;
	Timer finishTimer;
	Handler handler;	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//StaticOverall.setMainActivity(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading);
		
		//StaticOverall.netOperation = new NetOperation();
		iv_legoing = (ImageView)findViewById(R.id.ImageView_legoing);
		iv_star = (ImageView)findViewById(R.id.ImageView_loadingStar);
		//boolean isAvailable = StaticOverall.netOperation.IsNetworkAvailable();
		//boolean isServerConnectable = StaticOverall.netOperation.IsServerConnectable();
//		ParamObj paramObj = new ParamObj(){};
//		int ret = StaticOverall.netOperation.getSetInfo(100, paramObj);
//		Set set = (Set)paramObj.value;
//		System.out.println(set.SetName);
		
//		
		finishTimer = new Timer();
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO , Cxy, 2011-11-25 ����3:12:20
				switch (msg.what) {
				case 1:
					onTouchEvent(null);
					break;
				}
				super.handleMessage(msg);
			}
		};
		TimerTask task = new TimerTask()
		{
			@Override
			public void run() {
				// TODO , Cxy, 2011-11-25 ����3:13:55
				Message msgMessage = new Message();
				msgMessage.what = 1;
				handler.sendMessage(msgMessage);
			}
		};
		
		finishTimer.schedule(task, 3000);
		

	}
	@Override
	protected void onStart() {
		// TODO , Cxy, 2011-11-25 ����3:18:56
		Animation ani = AnimationUtils.loadAnimation(this, R.anim.scale_rotate);
		iv_legoing.startAnimation(ani);
		
		Animation ani2 = AnimationUtils.loadAnimation(this, R.anim.start_rotate);
		iv_star.startAnimation(ani2);
		super.onStart();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO , Cxy, 2011-10-23 ����9:59:14
		
		setResult(RESULT_OK);
		finish();
		return super.onTouchEvent(event);
	}

	

}