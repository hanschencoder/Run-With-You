package site.hanschen.runwithyou.ui.home.together;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.ui.home.devicelist.DeviceListActivity;
import site.hanschen.runwithyou.ui.home.doublerunner.DoubleRunnerActivity;


/**
 * @author HansChen
 */
public class TogetherFragment extends RunnerBaseFragment implements TogetherContract.View {

    @Nullable
    @Inject
    BluetoothAdapter  mBluetoothAdapter;
    @Inject
    TogetherPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerTogetherComponent.builder()
                               .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                               .togetherModule(new TogetherModule(TogetherFragment.this))
                               .build()
                               .inject(TogetherFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_together, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(TogetherFragment.this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.requestEnable();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_together_fragment, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.together_connect, R.id.together_make_discoverable})
    void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.together_connect:
                mPresenter.connectDevice();
                break;
            case R.id.together_make_discoverable:
                mPresenter.requestDiscoverable();
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void showBTUnavailable() {
        Toast.makeText(mContext.getApplicationContext(), "蓝牙不可用", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showBTSwitch(boolean enable) {
        if (enable) {
            Toast.makeText(getActivity(), "蓝牙已打开", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "蓝牙已关闭", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showDiscoverRefuseInfo() {
        Toast.makeText(getActivity(), "请求被拒绝", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectDeviceInfo() {
        startActivity(new Intent(getActivity(), DeviceListActivity.class));
    }

    @Override
    public void showDiscoverableInfo() {
        DoubleRunnerActivity.startAsServer(mContext);
    }

    @Override
    public void setPresenter(TogetherContract.Presenter presenter) {
        mPresenter = (TogetherPresenter) presenter;
    }
}
