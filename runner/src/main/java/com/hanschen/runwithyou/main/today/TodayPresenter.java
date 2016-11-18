package com.hanschen.runwithyou.main.today;

import android.os.RemoteException;

import com.hanschen.runwithyou.application.RunnerApplication;

import site.hanschen.common.utils.PreconditionUtils;

/**
 * @author HansChen
 */
public class TodayPresenter implements TodayContract.Presenter {

    private TodayContract.View mView;

    public TodayPresenter(TodayContract.View view) {
        this.mView = PreconditionUtils.checkNotNull(view, "TodayContract.View cannot be null!");
        mView.setPresenter(TodayPresenter.this);
    }

    @Override
    public void loadStepCount() {
        try {
            int count = RunnerApplication.getInstance().getRunnerManager().getStepCount();
            mView.onStepUpdateSuccess(count);
        } catch (RemoteException e) {
            e.printStackTrace();
            mView.onStepUpdateFailure();
        }
    }
}
