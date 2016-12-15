package site.hanschen.runwithyou.main.today;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.widget.CircleProgressBar;

/**
 * @author HansChen
 */
public class TodayFragment extends Fragment implements TodayContract.View {

    private Context           mContext;
    private CircleProgressBar mProgressBar;

    @Inject
    TodayPresenter mPresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (CircleProgressBar) view.findViewById(R.id.fragment_today_progress);
        mProgressBar.setMax(100);
        DaggerTodayComponent.builder().todayPresenterModule(new TodayPresenterModule(TodayFragment.this)).build().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadStepCount();
            }
        }, 300);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today1:
                break;
            case R.id.menu_today2:
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_today_fragment, menu);
    }

    @Override
    public void setPresenter(TodayContract.Presenter presenter) {
        mPresenter = (TodayPresenter) presenter;
    }

    @Override
    public void onStepUpdateSuccess(int count) {
        mProgressBar.setProgress(count);
    }

    @Override
    public void onStepUpdateFailure() {
        Toast.makeText(mContext, "获取步数失败", Toast.LENGTH_SHORT).show();
    }
}
