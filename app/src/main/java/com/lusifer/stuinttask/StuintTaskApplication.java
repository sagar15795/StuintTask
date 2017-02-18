package com.lusifer.stuinttask;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;
import android.content.Context;


public class StuintTaskApplication extends Application {

    private static StuintTaskApplication instance;

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

    }
}
