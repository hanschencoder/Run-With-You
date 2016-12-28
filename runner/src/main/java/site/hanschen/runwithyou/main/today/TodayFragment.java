package site.hanschen.runwithyou.main.today;

import android.os.Bundle;
import android.os.RemoteException;
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
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.service.RunnerCallback;
import site.hanschen.runwithyou.widget.CircleProgressBar;

/**
 * @author HansChen
 */
public class TodayFragment extends RunnerBaseFragment implements TodayContract.View {

    @Inject
    TodayPresenter mPresenter;
    private CircleProgressBar mProgressBar;

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
        DaggerTodayComponent.builder()
                            .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                            .todayPresenterModule(new TodayPresenterModule(TodayFragment.this))
                            .build()
                            .inject(this);
        try {
            RunnerApplication.getInstance().getRunnerManager().registerCallback(new RunnerCallback.Stub() {
                @Override
                public void onStepUpdate(int count) throws RemoteException {
                    mProgressBar.setProgress(count);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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
    public void onStepLoadSuccess(int count) {
        mProgressBar.setProgress(count);
    }

    @Override
    public void onStepLoadFailed() {
        Toast.makeText(mContext.getApplicationContext(), "获取步数失败", Toast.LENGTH_SHORT).show();
    }
}
