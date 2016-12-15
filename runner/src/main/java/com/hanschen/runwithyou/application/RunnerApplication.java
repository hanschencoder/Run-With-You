package com.hanschen.runwithyou.application;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hanschen.runwithyou.service.RunnerManager;
import com.hanschen.runwithyou.service.RunnerService;
import com.squareup.leakcanary.LeakCanary;

import site.hanschen.common.base.application.BaseApplication;

import static com.hanschen.runwithyou.utils.DexInstallHelper.isDexInstallProcess;
import static com.hanschen.runwithyou.utils.DexInstallHelper.isMultiDexInstalled;
import static com.hanschen.runwithyou.utils.DexInstallHelper.isVMMultiDexCapable;
import static com.hanschen.runwithyou.utils.DexInstallHelper.waitForDexInstall;

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
        if (isInWorkProcess(base)) {
            return;
        }
        // if VM has multi dex support, MultiDex support library is disabled
        if (!isVMMultiDexCapable()) {
            if (!isMultiDexInstalled(base)) {
                waitForDexInstall(base);
            }
            long start = System.currentTimeMillis();
            MultiDex.install(base);
            Log.d("Hans", "RunnerApplication#MultiDex.install: " + (System.currentTimeMillis() - start));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isInWorkProcess(this)) {
            return;
        }

        LeakCanary.install(this);
        sInstance = RunnerApplication.this;
        bindRunnerService();
    }

    /**
     * Return {@code true} if current process isn't main process.
     */
    private boolean isInWorkProcess(Context context) {
        return LeakCanary.isInAnalyzerProcess(context) || isDexInstallProcess(context);
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
