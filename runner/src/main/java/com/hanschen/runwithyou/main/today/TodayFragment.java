package com.hanschen.runwithyou.main.today;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanschen.runwithyou.R;
import com.hanschen.runwithyou.base.RunnerBaseActivity;
import com.hanschen.runwithyou.widget.CircleProgressBar;

/**
 * @author HansChen
 */
public class TodayFragment extends Fragment {

    private Context           mContext;
    private CircleProgressBar mProgressBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (CircleProgressBar) view.findViewById(R.id.fragment_today_progress);
        mProgressBar.setMax(100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mProgressBar.setProgress(((RunnerBaseActivity) mContext).getRunnerManager().getStepCount());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 300);
    }
}
