package site.hanschen.runwithyou.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import site.hanschen.runwithyou.eventbus.EventBus;

/**
 * @author HansChen
 */
public class RunnerBaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregisterCallback(RunnerBaseFragment.this);
    }
}
