package com.hanschen.runwithyou.application;


import android.content.Context;
import android.support.multidex.MultiDex;

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
    }

    @Override
    protected void initializeApplication() {

    }

    @Override
    protected void deInitializeApplication() {

    }
}
