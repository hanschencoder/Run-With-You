package site.hanschen.runwithyou.main.together;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

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
    TogetherPresenter(TogetherContract.View view, BluetoothAdapter adapter) {
        this.mView = PreconditionUtils.checkNotNull(view, "TogetherContract.View cannot be null!");
        this.mBluetoothAdapter = PreconditionUtils.checkNotNull(adapter, "BluetoothAdapter cannot be null!");
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void detach() {

    }

    @Override
    public boolean isBluetoothEnable() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    @Override
    public void requestBluetoothEnable() {
        if (mBluetoothAdapter == null) {
            mView.showBluetoothUnavailableTips();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mView.startActivityForResult(enableIntent, REQUEST_BT_ENABLE);
        }
    }

    @Override
    public void requestBluetoothDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            mView.startActivityForResult(discoverableIntent, REQUEST_BT_DISCOVERABLE);
        } else {
            mView.showBluetoothDiscoverableTips(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_BT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    mView.showBluetoothEnableTips(true);
                } else {
                    mView.showBluetoothEnableTips(false);
                }
                break;
            case REQUEST_BT_DISCOVERABLE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    mView.showBluetoothDiscoverableTips(false);
                } else {
                    mView.showBluetoothDiscoverableTips(true);
                }
                break;
        }
    }
}
