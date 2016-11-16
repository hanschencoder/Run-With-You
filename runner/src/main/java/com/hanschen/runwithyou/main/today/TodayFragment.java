package com.hanschen.runwithyou.main.today;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanschen.runwithyou.R;
import com.hanschen.runwithyou.widget.CircleProgressBar;

/**
 * @author HansChen
 */
public class TodayFragment extends Fragment {

    private CircleProgressBar mProgressBar;

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
        mProgressBar.setProgress(65);
    }
}
