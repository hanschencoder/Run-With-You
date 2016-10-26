package com.hanschen.common.base.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans.Chen
 */
public abstract class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    protected final List<Activity> mActivities    = new ArrayList<>();
    private         int            mActivityCount = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(BaseApplication.this);
    }

    protected abstract void initializeApplication();

    protected abstract void deInitializeApplication();

    public List<Activity> getActivities() {
        return mActivities;
    }

    public Activity getTopActivity() {
        return mActivities.get(mActivities.size() - 1);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (0 == mActivityCount) {
            initializeApplication();
        }
        mActivities.add(activity);
        mActivityCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
        mActivityCount--;
        if (0 == mActivityCount) {
            deInitializeApplication();
        }
    }
}
