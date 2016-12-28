package site.hanschen.runwithyou.main.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseFragment;


/**
 * @author HansChen
 */
public class DiscoverFragment extends RunnerBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discover_fragment, menu);
    }
}
