package site.hanschen.runwithyou.ui.home.doublerunner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import site.hanschen.common.statusbar.StatusBarCompat;
import site.hanschen.common.utils.ResourceUtils;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseActivity;
import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;

/**
 * @author HansChen
 */
public class DoubleRunnerActivity extends RunnerBaseActivity implements DoubleRunnerContract.View {

    private static final String KEY_MODE        = "KEY_MODE";
    private static final String KEY_DEVICE_INFO = "KEY_DEVICE_INFO";

    enum Mode {
        CLIENT,
        SERVER
    }

    public static void startAsServer(Context context) {
        Intent intent = new Intent(context, DoubleRunnerActivity.class);
        intent.putExtra(KEY_MODE, Mode.SERVER);
        context.startActivity(intent);
    }

    public static void startAsClient(Context context, Device device) {
        Intent intent = new Intent(context, DoubleRunnerActivity.class);
        intent.putExtra(KEY_MODE, Mode.CLIENT);
        intent.putExtra(KEY_DEVICE_INFO, device);
        context.startActivity(intent);
    }

    private Mode           mMode;
    private Device         mDevice;
    private RelativeLayout mConnectingInfo;
    @Inject
    DoubleRunnerPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_runner);
        StatusBarCompat.setColor(this, ResourceUtils.getColor(mContext, R.color.background_double_runner), 0);
        parseData();
        initViews();
        DaggerDoubleRunnerComponent.builder()
                                   .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                                   .doubleRunnerModule(new DoubleRunnerModule(DoubleRunnerActivity.this))
                                   .build()
                                   .inject(DoubleRunnerActivity.this);
        if (mMode == Mode.CLIENT) {
            mPresenter.connect(mDevice);
        } else {
            mPresenter.listen((int) TimeUnit.SECONDS.toMillis(30));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    private void initViews() {
        mConnectingInfo = findView(R.id.double_runner_connecting_info);
    }

    private void parseData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || (mMode = (Mode) bundle.getSerializable(KEY_MODE)) == null) {
            throw new IllegalStateException("bundle must contain mode info");
        }

        if (mMode == Mode.CLIENT && (mDevice = (Device) bundle.getSerializable(KEY_DEVICE_INFO)) == null) {
            throw new IllegalStateException("bundle must contain device info when start as client mode");
        }
    }

    @Override
    public void setPresenter(DoubleRunnerContract.Presenter presenter) {
        mPresenter = (DoubleRunnerPresenter) presenter;
    }

    @Override
    public void showConnectingInfo() {
        mConnectingInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void showConnectFailedInfo() {
        mConnectingInfo.setVisibility(View.GONE);
        Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectedInfo(Device device) {
        mConnectingInfo.setVisibility(View.GONE);
        Toast.makeText(mContext, String.format("连接成功: %s", device.getName()), Toast.LENGTH_SHORT).show();
    }
}
