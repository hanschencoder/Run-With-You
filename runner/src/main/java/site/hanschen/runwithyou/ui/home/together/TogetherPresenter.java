package site.hanschen.runwithyou.ui.home.together;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import site.hanschen.common.utils.PreconditionUtils;

/**
 * @author HansChen
 */
class TogetherPresenter implements TogetherContract.Presenter {

    private static final int REQUEST_BT_ENABLE       = 1;
    private static final int REQUEST_BT_DISCOVERABLE = 2;

    private BluetoothAdapter      mBluetoothAdapter;
    private TogetherContract.View mView;

    @Inject
    TogetherPresenter(TogetherContract.View view, @Nullable BluetoothAdapter adapter) {
        this.mView = PreconditionUtils.checkNotNull(view, "TogetherContract.View cannot be null!");
        this.mBluetoothAdapter = adapter;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void detach() {

    }

    @Override
    public void requestEnable() {
        if (mBluetoothAdapter == null) {
            mView.showBTUnavailable();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mView.startActivityForResult(enableIntent, REQUEST_BT_ENABLE);
        }
    }

    @Override
    public void connectDevice() {
        if (!isBluetoothEnable()) {
            requestEnable();
        } else {
            mView.showConnectDeviceInfo();
        }
    }

    @Override
    public void requestDiscoverable() {
        if (mBluetoothAdapter == null) {
            mView.showBTUnavailable();
            return;
        }
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            mView.startActivityForResult(discoverableIntent, REQUEST_BT_DISCOVERABLE);
        } else {
            mView.showDiscoverableInfo();
        }
    }

    private boolean isBluetoothEnable() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_BT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    mView.showBTSwitch(true);
                } else {
                    mView.showBTSwitch(false);
                }
                break;
            case REQUEST_BT_DISCOVERABLE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    mView.showDiscoverRefuseInfo();
                } else {
                    mView.showDiscoverableInfo();
                }
                break;
        }
    }
}
