package com.hanschen.common.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * @author Hans.Chen
 */
public class BaseActivity extends AppCompatActivity {

    protected Context        mContext;
    private   Handler        mMainHandler;
    private   MaterialDialog mWaitingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private boolean isWaitingDialogShowing() {
        return mWaitingDialog != null && mWaitingDialog.isShowing();
    }

    public void showWaitingDialog() {
        if (!isWaitingDialogShowing()) {
            mWaitingDialog = new MaterialDialog.Builder(mContext).progress(true, 0).progressIndeterminateStyle(true).build();
            mWaitingDialog.setCancelable(false);
            mWaitingDialog.show();
        }
    }

    public void dismissWaitingDialog() {
        if (isWaitingDialogShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    protected Handler getMainHandler() {
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        return mMainHandler;
    }
}
