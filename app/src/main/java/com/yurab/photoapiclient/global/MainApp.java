package com.yurab.photoapiclient.global;

import android.app.Application;
import android.content.Context;


public class MainApp extends Application {

    private static MainApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext() {
        return mInstance.getBaseContext();
    }
}
