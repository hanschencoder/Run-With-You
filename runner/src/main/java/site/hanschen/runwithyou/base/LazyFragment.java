package site.hanschen.runwithyou.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

/**
 * lazy load data until fragment is visible. It is useful when use fragment in {@link android.support.v4.view.ViewPager}
 *
 * @author HansChen
 */
public abstract class LazyFragment extends RunnerBaseFragment {


    private boolean isFragmentCreated = false;
    private boolean isFirstVisible    = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isFragmentCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        attemptPerformOnUserVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.isFragmentCreated = false;
        this.isFirstVisible = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            attemptPerformOnUserVisible();
        } else {
            if (!isFirstVisible) {
                // invoke only when fragment have been visible
                onUserInvisible();
            }
        }
    }

    /**
     * call me when fragment is onResume and visible to user, because the invoke order of {@link Fragment#setUserVisibleHint(boolean)}
     * and {@link Fragment#onResume()} (Bundle)} is uncertainty.
     */
    private void attemptPerformOnUserVisible() {
        if (getUserVisibleHint() && isFragmentCreated) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onFirstUserVisible();
            } else {
                onUserVisible();
            }
        }
    }

    /**
     * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
     */
    protected abstract void onFirstUserVisible();

    /**
     * this method invoke when fragment is visible to user
     */
    protected void onUserVisible() {

    }

    /**
     * this method invoke when fragment is invisible to user
     */
    protected void onUserInvisible() {

    }
}
