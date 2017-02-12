package site.hanschen.runwithyou.main.together;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
public class DoubleRunnerActivity extends RunnerBaseActivity {

    private static final String KEY_MODE        = "KEY_MODE";
    private static final String KEY_DEVICE_INFO = "KEY_DEVICE_INFO";

    enum Mode {
        CLIENT,
        SERVER
    }

    public static void startAsClient(Context context) {
        Intent intent = new Intent(context, DoubleRunnerActivity.class);
        intent.putExtra(KEY_MODE, Mode.CLIENT);
        context.startActivity(intent);
    }

    public static void startAsServer(Context context, Device device) {
        Intent intent = new Intent(context, DoubleRunnerActivity.class);
        intent.putExtra(KEY_MODE, Mode.SERVER);
        intent.putExtra(KEY_DEVICE_INFO, device);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_runner);
        StatusBarCompat.setColor(this, ResourceUtils.getColor(mContext, R.color.double_runner_bg), 0);
    }
}
