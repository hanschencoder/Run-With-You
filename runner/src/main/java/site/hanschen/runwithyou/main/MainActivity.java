package site.hanschen.runwithyou.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.main.discover.DiscoverFragment;
import site.hanschen.runwithyou.main.me.MeFragment;
import site.hanschen.runwithyou.main.today.TodayFragment;
import site.hanschen.runwithyou.main.together.TogetherFragment;
import site.hanschen.runwithyou.utils.StepCountUtils;


public class MainActivity extends RunnerBaseActivity implements OnTabSelectListener {

    private Toolbar   mToolbar;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        checkStepCountSupport();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        mBottomBar = (BottomBar) findViewById(R.id.main_bottomBar);
        mBottomBar.setOnTabSelectListener(MainActivity.this);
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
        // attention here, don't add exitAnim for replace fragment, otherwise when fast switch fragment would have problem.
        // because fragment will not remove before animator is end
        switch (tabId) {
            case R.id.tab_today:
                replaceFragment(R.id.main_fragment_container, TodayFragment.class, R.animator.fade_in, 0);
                break;
            case R.id.tab_together:
                replaceFragment(R.id.main_fragment_container, TogetherFragment.class, R.animator.fade_in, 0);
                break;
            case R.id.tab_discover:
                replaceFragment(R.id.main_fragment_container, DiscoverFragment.class, R.animator.fade_in, 0);
                break;
            case R.id.tab_me:
                replaceFragment(R.id.main_fragment_container, MeFragment.class, R.animator.fade_in, 0);
                break;
            default:
                break;
        }
    }
}
