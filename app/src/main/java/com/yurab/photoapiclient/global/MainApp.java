package com.yurab.photoapiclient.global;

import android.app.Application;
import android.content.Context;


public class MainApp extends Application {

    // i know it would be better not to create static context references
    //and use dagger 2 instead...
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
