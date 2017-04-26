package site.hanschen.runwithyou.ui.home.devicelist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.LazyFragment;
import site.hanschen.runwithyou.ui.home.devicelist.adapter.DeviceListAdapter;
import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;
import site.hanschen.runwithyou.ui.home.doublerunner.DoubleRunnerActivity;

/**
 * @author HansChen
 */
public class DeviceFragment extends LazyFragment implements DeviceListContract.View, View.OnClickListener {

    private static String KEY_CATEGORY = "KEY_CATEGORY";

    public static DeviceFragment newInstance(DeviceCategory category) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.device_list_devices)
    RecyclerView mRecyclerView;
    @BindView(R.id.device_list_discovery)
    Button       mDiscoveryBtn;
    @BindView(R.id.device_list_discovery_status)
    ProgressBar  mDiscoveryStatus;

    private DeviceCategory mCategory;
    private List<Device> mDevices = new ArrayList<>();
    private DeviceListAdapter mAdapter;
    @Inject
    DeviceListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !getArguments().containsKey(KEY_CATEGORY)) {
            throw new IllegalStateException("bundle must contain category info");
        }
        mCategory = (DeviceCategory) getArguments().getSerializable(KEY_CATEGORY);

        DaggerDeviceListComponent.builder()
                                 .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                                 .deviceListModule(new DeviceListModule(DeviceFragment.this))
                                 .build()
                                 .inject(DeviceFragment.this);
        if (mCategory == DeviceCategory.NEW) {
            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCategory == DeviceCategory.NEW) {
            getActivity().unregisterReceiver(mReceiver);
        }
        mPresenter.cancelDiscovery();
        mPresenter.detach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mCategory == DeviceCategory.NEW) {
            setHasOptionsMenu(true);
        }
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today1:
                break;
            case R.id.menu_today2:
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mCategory == DeviceCategory.NEW) {
            inflater.inflate(R.menu.menu_today_fragment, menu);
        }
    }

    private void initViews() {
        if (mCategory == DeviceCategory.NEW) {
            mDiscoveryBtn.setVisibility(View.VISIBLE);
        } else {
            mDiscoveryBtn.setVisibility(View.GONE);
        }
        mDiscoveryBtn.setOnClickListener(DeviceFragment.this);
        mDiscoveryStatus.setVisibility(View.GONE);
        mDiscoveryStatus.setIndeterminate(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DeviceListAdapter(mContext);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private DeviceListAdapter.OnItemClickListener mOnItemClickListener = new DeviceListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final Device device) {
            new MaterialDialog.Builder(mContext).title("连接设备")
                                                .content(String.format("尝试连接设备[设备名:%s, 地址:%s] ?",
                                                                       device.getName(),
                                                                       device.getAddress()))
                                                .positiveText("连接")
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog,
                                                                        @NonNull DialogAction which) {
                                                        DoubleRunnerActivity.startAsClient(mContext, device);
                                                    }
                                                })
                                                .negativeText("取消")
                                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog,
                                                                        @NonNull DialogAction which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .build()
                                                .show();
        }
    };

    @Override
    protected void onFirstUserVisible() {
        if (mCategory == DeviceCategory.PAIRED) {
            mPresenter.loadPairedDevices();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_list_discovery:
                new MaterialDialog.Builder(mContext).title("发现设备")
                                                    .content("请先在另一台设备点击[等待连接], 然后点击[发现]按钮")
                                                    .positiveText("发现")
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog,
                                                                            @NonNull DialogAction which) {
                                                            mPresenter.discoveryDevices();
                                                        }
                                                    })
                                                    .negativeText("取消")
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog,
                                                                            @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .build()
                                                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(DeviceListContract.Presenter presenter) {
        mPresenter = (DeviceListPresenter) presenter;
    }

    @Override
    public void showAllDevice(Set<Device> devices) {
        mDevices.clear();
        mDevices.addAll(devices);
        mAdapter.setData(mDevices);
    }

    @Override
    public void addNewDevice(Device device) {
        if (!mDevices.contains(device)) {
            mDevices.add(device);
            mAdapter.setData(mDevices);
        }
    }

    @Override
    public void showDiscoveryStartInfo() {
        mDiscoveryBtn.setVisibility(View.GONE);
        mDiscoveryStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDiscoveryFinishedInfo() {
        mDiscoveryBtn.setVisibility(View.VISIBLE);
        mDiscoveryStatus.setVisibility(View.GONE);
        mDiscoveryBtn.setText("再次查找设备");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addNewDevice(new Device(device.getName(), device.getAddress()));
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                showDiscoveryFinishedInfo();
            }
        }
    };
}
