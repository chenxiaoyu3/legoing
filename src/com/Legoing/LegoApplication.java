package com.Legoing;

import android.app.Application;

public class LegoApplication extends Application{

    
    @Override
    public void onCreate() {
        // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 23, 2012 5:09:22 PM
        super.onCreate();
        Activity_Base.init();
    }
}
