package com.Legoing.UserControls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class UCtrl_Loading extends ProgressBar
{
	Context context;
	public UCtrl_Loading(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		
		this.context = context;
		super.setMax(100);
		super.setProgress(0);
		
	}
		
	private boolean go = true;
	protected static final int NEXT = 0x10001;
	private int iCount = 0;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEXT:
				if (!Thread.currentThread().isInterrupted()) {
					UCtrl_Loading.this.setProgress(iCount);
				}
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};
	
		
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO , Cxy, 2011-11-2 ÏÂÎç10:22:44
			int i = 0;
			while(go)
				try {
					iCount = (i++) * 5 % 100;
					Thread.sleep(1000);
					Message msg = new Message();
					msg.what = NEXT;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}
	};
	Thread mThread = new Thread(runnable);
	public void start()
	{
		try {
			mThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void stop()
	{
		go = false;
		mThread = new Thread(runnable);
	}
	

}
