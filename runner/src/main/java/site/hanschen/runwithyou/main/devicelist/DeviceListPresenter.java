package site.hanschen.runwithyou.main.devicelist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import site.hanschen.common.utils.PreconditionUtils;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
class DeviceListPresenter implements DeviceListContract.Presenter {

    private BluetoothAdapter        mBtAdapter;
    private DeviceListContract.View mView;

    @Inject
    DeviceListPresenter(DeviceListContract.View view, @Nullable BluetoothAdapter btAdapter) {
        this.mView = PreconditionUtils.checkNotNull(view, "DeviceListContract.View cannot be null!");
        assert btAdapter != null;
        this.mBtAdapter = PreconditionUtils.checkNotNull(btAdapter, "BluetoothAdapter cannot be null!");
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void detach() {

    }

    @Override
    public void loadHistoryDevices() {

    }

    @Override
    public void loadPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        Set<Device> devices = new HashSet<>();
        for (BluetoothDevice device : pairedDevices) {
            devices.add(new Device(device.getName(), device.getAddress()));
        }
        mView.showAllDevice(devices);
    }

    @Override
    public void discoveryDevices() {
        mView.showDiscoveryStartInfo();
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    @Override
    public void cancelDiscovery() {
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
    }
}
