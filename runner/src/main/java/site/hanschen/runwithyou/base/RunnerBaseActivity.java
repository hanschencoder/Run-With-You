package site.hanschen.runwithyou.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import site.hanschen.common.base.activity.BaseActivity;
import site.hanschen.runwithyou.eventbus.EventBus;

/**
 * @author HansChen
 */
public class RunnerBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregisterCallback(RunnerBaseActivity.this);
    }
}
