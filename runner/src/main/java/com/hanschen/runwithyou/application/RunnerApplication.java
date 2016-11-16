package com.hanschen.runwithyou.application;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.multidex.MultiDex;

import com.hanschen.runwithyou.service.RunnerManager;
import com.hanschen.runwithyou.service.RunnerService;
import com.squareup.leakcanary.LeakCanary;

import site.hanschen.common.base.application.BaseApplication;

/**
 * @author HansChen
 */
public class RunnerApplication extends BaseApplication {

    private static RunnerApplication sInstance;
    private        RunnerManager     mRunnerManager;

    public static RunnerApplication getInstance() {
        return sInstance;
    }

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
        sInstance = RunnerApplication.this;
        bindRunnerService();
    }

    @Override
    protected void initializeApplication() {

    }

    @Override
    protected void deInitializeApplication() {

    }

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRunnerManager = RunnerManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRunnerManager = null;
        }
    };

    private void bindRunnerService() {
        RunnerService.bind(getApplicationContext(), mConn);
    }

    private void unbindRunnerService() {
        RunnerService.unbind(getApplicationContext(), mConn);
        mRunnerManager = null;
    }

    public RunnerManager getRunnerManager() {
        if (mRunnerManager == null) {
            throw new IllegalStateException("mRunnerManager is null now ");
        }
        return mRunnerManager;
    }
}
