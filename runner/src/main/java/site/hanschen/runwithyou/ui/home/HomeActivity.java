package site.hanschen.runwithyou.ui.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.afollestad.materialdialogs.MaterialDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.home.discover.DiscoverFragment;
import site.hanschen.runwithyou.ui.home.setting.SettingFragment;
import site.hanschen.runwithyou.ui.home.today.TodayFragment;
import site.hanschen.runwithyou.ui.home.together.TogetherFragment;
import site.hanschen.runwithyou.utils.StepCountUtils;

/**
 * @author HansChen
 */
public class HomeActivity extends RunnerBaseActivity implements OnTabSelectListener {

    private Toolbar                                mToolbar;
    private BottomBar                              mBottomBar;
    private SparseArray<Class<? extends Fragment>> mMap;
    private int                                    mPreSelectedTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        initViews();
        checkStepCountSupport();
    }

    private void initData() {
        mMap = new SparseArray<>(4);
        mMap.put(R.id.tab_today, TodayFragment.class);
        mMap.put(R.id.tab_together, TogetherFragment.class);
        mMap.put(R.id.tab_discover, DiscoverFragment.class);
        mMap.put(R.id.setting, SettingFragment.class);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        mBottomBar = (BottomBar) findViewById(R.id.home_bottomBar);
        mBottomBar.setOnTabSelectListener(HomeActivity.this);
    }

    private void checkStepCountSupport() {
        if (!StepCountUtils.isStepCountSupport(mContext)) {
            new MaterialDialog.Builder(mContext).title(R.string.sorry)
                                                .content(R.string.not_support_step_count)
                                                .positiveText(R.string.i_see)
                                                .build()
                                                .show();
        }
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        if (mPreSelectedTab != tabId) {
            Class<? extends Fragment> clz = mMap.get(mPreSelectedTab);
            if (clz != null) {
                hideFragment(clz);
            }

            clz = mMap.get(tabId);
            if (clz != null) {
                showFragment(R.id.home_fragment_container, clz, R.animator.fade_in, 0);
            }

            mPreSelectedTab = tabId;

            if (tabId == R.id.setting) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
    }
}
