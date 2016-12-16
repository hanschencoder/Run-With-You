package site.hanschen.runwithyou.startup;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import site.hanschen.common.base.activity.BaseActivity;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.main.MainActivity;
import site.hanschen.runwithyou.service.RunnerService;

/**
 * @author HansChen
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        RunnerService.bind(getApplicationContext(), mConn);
    }

    @Override
    public void onBackPressed() {
        // not allow back
    }

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RunnerService.unbind(getApplicationContext(), mConn);
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }, TimeUnit.SECONDS.toMillis(1));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
