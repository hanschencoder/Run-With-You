package site.hanschen.runwithyou.startup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.util.Log;

import site.hanschen.common.base.activity.BaseActivity;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.utils.DexInstallHelper;


/**
 * only display when application is start for first time or application has been updated
 * it would run in dexInstall process
 *
 * @author HansChen
 */
public class DexInstallActivity extends BaseActivity implements DexInstallCallback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dex_install);

        new Thread(new DexInstallTask(mContext, DexInstallActivity.this)).start();
    }

    @Override
    public void onBackPressed() {
        // not allow back
    }

    @Override
    public void onInstallStart() {
        Log.d("Hans", "onInstallStart");
    }

    @Override
    public void onInstallComplete() {
        Log.d("Hans", "onInstallComplete");
        DexInstallHelper.markInstallFinish(getApplicationContext());
        finish();
        System.exit(0);
    }

    private static class DexInstallTask implements Runnable {

        private final Context            mAppContext;
        private final DexInstallCallback mCallback;

        DexInstallTask(Context context, DexInstallCallback callback) {
            if (context == null || callback == null) {
                throw new IllegalArgumentException("context == null || callback == null");
            }
            this.mAppContext = context.getApplicationContext();
            this.mCallback = callback;
        }

        @Override
        public void run() {

            mCallback.onInstallStart();
            MultiDex.install(mAppContext);
            mCallback.onInstallComplete();
        }
    }
}
