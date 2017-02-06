package site.hanschen.runwithyou.main.devicelist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.base.RunnerBaseFragment;
import site.hanschen.runwithyou.main.devicelist.adapter.DeviceListAdapter;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
public class DeviceFragment extends RunnerBaseFragment implements DeviceListContract.View {

    private static String KEY_CATEGORY = "KEY_CATEGORY";

    public static DeviceFragment newInstance(DeviceCategory category) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    private DeviceCategory mCategory;
    private List<Device> mDevices = new ArrayList<>();
    private RecyclerView      mRecyclerView;
    private DeviceListAdapter mAdapter;
    private MaterialDialog    mWaitingDialog;
    @Inject
    DeviceListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !getArguments().containsKey(KEY_CATEGORY)) {
            throw new IllegalStateException("bundle must contain category info");
        }
        mCategory = (DeviceCategory) getArguments().getSerializable(KEY_CATEGORY);
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
        initViews(view);
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

    private void initViews(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.device_list_devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DeviceListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerDeviceListComponent.builder()
                                 .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                                 .deviceListModule(new DeviceListModule(DeviceFragment.this))
                                 .build()
                                 .inject(DeviceFragment.this);
        if (mCategory == DeviceCategory.PAIRED) {
            mPresenter.loadPairedDevices();
        } else if (mCategory == DeviceCategory.NEW) {
            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(mReceiver, filter);

            mPresenter.discoveryDevices();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCategory == DeviceCategory.NEW) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    private boolean isWaitingDialogShowing() {
        return mWaitingDialog != null && mWaitingDialog.isShowing();
    }

    public void showWaitingDialog(String title, String content) {
        if (!isWaitingDialogShowing()) {
            mWaitingDialog = new MaterialDialog.Builder(mContext).progress(true, 0)
                                                                 .progressIndeterminateStyle(true)
                                                                 .title(title)
                                                                 .content(content)
                                                                 .build();
            mWaitingDialog.setCancelable(false);
            mWaitingDialog.show();
        }
    }

    public void dismissWaitingDialog() {
        if (isWaitingDialogShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    @Override
    public void setPresenter(DeviceListContract.Presenter presenter) {
        mPresenter = (DeviceListPresenter) presenter;
    }

    @Override
    public void onDeviceLoaded(Set<Device> devices) {
        mDevices.clear();
        mDevices.addAll(devices);
        mAdapter.setData(mDevices);
    }

    @Override
    public void onNewDeviceFound(Device device) {
        if (!mDevices.contains(device)) {
            mDevices.add(device);
            mAdapter.setData(mDevices);
        }
    }

    @Override
    public void onDiscoveryStart() {
        Toast.makeText(mContext.getApplicationContext(), "onDiscoveryStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDiscoveryFinished() {
        Toast.makeText(mContext.getApplicationContext(), "onDiscoveryFinished", Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onNewDeviceFound(new Device(device.getName(), device.getAddress()));
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                onDiscoveryFinished();
            }
        }
    };
}
