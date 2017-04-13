package site.hanschen.runwithyou.ui.home.devicelist.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

import site.hanschen.runwithyou.ui.home.devicelist.DeviceCategory;
import site.hanschen.runwithyou.ui.home.devicelist.DeviceFragment;

/**
 * @author HansChen
 */
public class DevicePagerAdapter extends FragmentPagerAdapter {

    private List<DeviceCategory> mCategories;

    public DevicePagerAdapter(FragmentManager fm, List<DeviceCategory> categories) {
        super(fm);
        this.mCategories = categories;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (mCategories.get(position)) {
            case HISTORY:
                return "历史设备";
            case PAIRED:
                return "已配对";
            case NEW:
                return "新设备";
            default:
                throw new IllegalStateException("unknown category: " + mCategories.get(position));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return DeviceFragment.newInstance(mCategories.get(position));
    }
}
