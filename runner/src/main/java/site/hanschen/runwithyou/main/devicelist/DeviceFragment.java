package site.hanschen.runwithyou.main.devicelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
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
        }
    }

    @Override
    public void setPresenter(DeviceListContract.Presenter presenter) {
        mPresenter = (DeviceListPresenter) presenter;
    }

    @Override
    public void onDeviceLoaded(List<Device> devices) {
        mDevices.clear();
        mDevices.addAll(devices);
        mAdapter.setData(mDevices);
    }
}
