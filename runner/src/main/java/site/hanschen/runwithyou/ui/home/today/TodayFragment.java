package site.hanschen.runwithyou.ui.home.today;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.eventbus.EventBus;
import site.hanschen.runwithyou.eventbus.OnStepCallback;
import site.hanschen.runwithyou.widget.CircleProgressBar;

/**
 * @author HansChen
 */
public class TodayFragment extends RunnerBaseFragment implements TodayContract.View {

    @Inject
    TodayPresenter    mPresenter;
    @BindView(R.id.fragment_today_progress)
    CircleProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerTodayComponent.builder()
                            .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                            .todayPresenterModule(new TodayPresenterModule(TodayFragment.this))
                            .build()
                            .inject(this);
        EventBus.getInstance().registerStepCallback(TodayFragment.this, mOnStepCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
        EventBus.getInstance().unregisterCallback(TodayFragment.this);
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
        ButterKnife.bind(this, view);
    }

    private OnStepCallback mOnStepCallback = new OnStepCallback() {
        @Override
        public void onStepUpdate(long count) {
            showCurrentStepCount(count);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadStepCount();
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
    public void showMaxCount(long max) {
        mProgressBar.setMax((int) max);
        mProgressBar.setSubText(String.format(Locale.getDefault(), "今天目标：%d步", max));
    }

    @Override
    public void showCurrentStepCount(long count) {
        mProgressBar.setProgress(count);
    }

    @Override
    public void showStepLoadFailInfo() {
        Toast.makeText(mContext.getApplicationContext(), "获取步数失败", Toast.LENGTH_SHORT).show();
    }
}
