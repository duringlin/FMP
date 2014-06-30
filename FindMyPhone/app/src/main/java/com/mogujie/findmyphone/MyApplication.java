package com.mogujie.findmyphone;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Norsie on 14-6-28.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
