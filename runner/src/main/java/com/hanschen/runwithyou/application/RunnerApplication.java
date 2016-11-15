package com.hanschen.runwithyou.application;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

import site.hanschen.common.base.application.BaseApplication;

/**
 * @author HansChen
 */
public class RunnerApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    @Override
    protected void initializeApplication() {

    }

    @Override
    protected void deInitializeApplication() {

    }
}
