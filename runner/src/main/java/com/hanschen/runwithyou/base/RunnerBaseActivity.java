package com.hanschen.runwithyou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hanschen.runwithyou.application.RunnerApplication;
import com.hanschen.runwithyou.service.RunnerManager;

import site.hanschen.common.base.activity.BaseActivity;

/**
 * @author HansChen
 */
public class RunnerBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public RunnerManager getRunnerManager() {
        return RunnerApplication.getInstance().getRunnerManager();
    }
}
