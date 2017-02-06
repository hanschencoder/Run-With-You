package site.hanschen.runwithyou.main.together;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.main.devicelist.DeviceListActivity;


/**
 * @author HansChen
 */
public class TogetherFragment extends RunnerBaseFragment implements View.OnClickListener, TogetherContract.View {

    private Button mConnectBtn;
    private Button mDiscoverableBtn;
    @Inject
    BluetoothAdapter  mBluetoothAdapter;
    @Inject
    TogetherPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mConnectBtn = (Button) view.findViewById(R.id.together_connect);
        mConnectBtn.setOnClickListener(this);
        mDiscoverableBtn = (Button) view.findViewById(R.id.together_make_discoverable);
        mDiscoverableBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerTogetherComponent.builder()
                               .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                               .togetherModule(new TogetherModule(TogetherFragment.this))
                               .build()
                               .inject(TogetherFragment.this);
        mPresenter.requestBluetoothEnable();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_together_fragment, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.together_connect:
                startActivity(new Intent(getActivity(), DeviceListActivity.class));
                break;
            case R.id.together_make_discoverable:
                mPresenter.requestBluetoothDiscoverable();
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onBluetoothUnavailable() {
        Toast.makeText(mContext.getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBluetoothEnable(boolean enable) {
        if (enable) {
            Toast.makeText(getActivity(), "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBluetoothDiscoverable(boolean discoverable) {
        if (discoverable) {
            Toast.makeText(getActivity(), "Bluetooth is discoverable now", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "user has rejected the request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(TogetherContract.Presenter presenter) {
        mPresenter = (TogetherPresenter) presenter;
    }
}
