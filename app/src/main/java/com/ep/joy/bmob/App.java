package com.ep.joy.bmob;

import android.app.Application;

import com.jiongbull.jlog.JLog;

/**
 * author   Joy
 * Date:  2016/7/14.
 * version:  V1.0
 * Description:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JLog.init(this)
                .setDebug(BuildConfig.DEBUG);
    }
}
