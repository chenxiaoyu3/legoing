package com.Legoing.UserControls;

import com.Legoing.Activity_Base;
import com.Legoing.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class UCtrl_AnimTabHost extends TabHost{

	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private int tabCount;//tab页总数
	
	
	Context context;
	public UCtrl_AnimTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// _TODO , Cxy, 2011-11-4 下午7:11:09
		this.context = context;
		slideLeftIn = AnimationUtils.loadAnimation(context,R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context,R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context,R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,R.anim.slide_right_out);
	}
	
	public int getTabCount() {
		return tabCount;
	}
	@Override
	public void addTab(TabSpec tabSpec) {
		tabCount++;
		super.addTab(tabSpec);
	}
	
	@Override
	public void setCurrentTab(int index) {
		//index为要切换到的tab页索引，currentTabIndex为现在要当前tab页的索引
		int currentTabIndex = getCurrentTab();
		
		//设置当前tab页退出时的动画
		if (null != getCurrentView()){//第一次进入MainActivity时，getCurrentView()取得的值为空
			if (currentTabIndex == (tabCount - 1) && index == 0) {//处理边界滑动
				getCurrentView().startAnimation(slideLeftOut);
			} else if (currentTabIndex == 0 && index == (tabCount - 1)) {//处理边界滑动
				getCurrentView().startAnimation(slideRightOut);
			} else if (index > currentTabIndex) {//非边界情况下从右往左fleep
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < currentTabIndex) {//非边界情况下从左往右fleep
				getCurrentView().startAnimation(slideRightOut);
			}
		}
		
		super.setCurrentTab(index);
		View curView = getCurrentView();
		if (curView == null) {
			return;
		}
		//设置即将显示的tab页的动画
		if (currentTabIndex == (tabCount - 1) && index == 0){//处理边界滑动
			getCurrentView().startAnimation(slideLeftIn);
		} else if (currentTabIndex == 0 && index == (tabCount - 1)) {//处理边界滑动
			getCurrentView().startAnimation(slideRightIn);
		} else if (index > currentTabIndex) {//非边界情况下从右往左fleep
			getCurrentView().startAnimation(slideLeftIn);
		} else if (index < currentTabIndex) {//非边界情况下从左往右fleep
			getCurrentView().startAnimation(slideRightIn);
		}
	
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 24, 2012 7:11:46 PM
	    boolean ret = super.dispatchKeyEvent(event);
	    if (!ret) {
	        //上层没有处理
            if( event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            {
                ((Activity_Base)context).shutdownConfirm();
                return true;
            }
        }else {
            //上层处理了
            return true;
        }
	    return false;
	}

}
