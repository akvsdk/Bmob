package com.ep.joy.bmob;

import android.app.Application;
import android.content.Context;

import com.jiongbull.jlog.JLog;

/**
 * author   Joy
 * Date:  2016/7/14.
 * version:  V1.0
 * Description:
 */
public class App extends Application {
    private static Context mContext;
    public static Context getContext() {
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        JLog.init(this)
                .setDebug(BuildConfig.DEBUG);
    }
}
