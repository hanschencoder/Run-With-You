package site.hanschen.runwithyou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import site.hanschen.common.base.activity.BaseActivity;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.service.RunnerManager;

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
