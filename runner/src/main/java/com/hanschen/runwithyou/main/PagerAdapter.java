package com.hanschen.runwithyou.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author HansChen
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private Context         mContext;
    private FragmentManager mManager;
    private List<Fragment>  mFragments;

    public PagerAdapter(Context context, FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.mManager = fm;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
