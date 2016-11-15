package com.hanschen.runwithyou.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.hanschen.runwithyou.R;
import com.hanschen.runwithyou.main.discover.DiscoverFragment;
import com.hanschen.runwithyou.main.me.MeFragment;
import com.hanschen.runwithyou.main.today.TodayFragment;
import com.hanschen.runwithyou.main.together.TogetherFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import site.hanschen.common.base.activity.BaseActivity;


public class MainActivity extends BaseActivity implements OnTabSelectListener {

    private Toolbar   mToolbar;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);


        mBottomBar = (BottomBar) findViewById(R.id.main_bottomBar);
        mBottomBar.setOnTabSelectListener(MainActivity.this);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        //attention here, don't add exitAnim for replace fragment, otherwise when fast switch fragment would have problem.
        //because fragment will not remove before animator is end
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
