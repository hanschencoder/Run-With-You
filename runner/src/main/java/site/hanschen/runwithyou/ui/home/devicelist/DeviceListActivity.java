package site.hanschen.runwithyou.ui.home.devicelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.home.devicelist.adapter.DevicePagerAdapter;

/**
 * @author HansChen
 */
public class DeviceListActivity extends RunnerBaseActivity {

    @BindView(R.id.device_list_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.device_list_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_list_toolbar);
        toolbar.setTitle("连接设备");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager.setOffscreenPageLimit(2);
        DeviceCategory[] categories = new DeviceCategory[]{
                DeviceCategory.HISTORY, DeviceCategory.PAIRED, DeviceCategory.NEW};
        mViewPager.setAdapter(new DevicePagerAdapter(getFragmentManager(), Arrays.asList(categories)));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
