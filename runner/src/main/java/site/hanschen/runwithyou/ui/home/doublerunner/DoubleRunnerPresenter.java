package site.hanschen.runwithyou.ui.home.doublerunner;

import javax.inject.Inject;

import site.hanschen.common.utils.PreconditionUtils;
import site.hanschen.runwithyou.bluetooth.BluetoothControler;
import site.hanschen.runwithyou.bluetooth.BluetoothListener;
import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;

/**
 * @author HansChen
 */
class DoubleRunnerPresenter implements DoubleRunnerContract.Presenter {

    private DoubleRunnerContract.View mView;
    private BluetoothControler        mControler;

    @Inject
    DoubleRunnerPresenter(DoubleRunnerContract.View view, BluetoothControler bluetoothControler) {
        this.mView = PreconditionUtils.checkNotNull(view, "DoubleRunnerContract.View cannot be null!");
        this.mControler = PreconditionUtils.checkNotNull(bluetoothControler, "BluetoothControler cannot be null!");
        this.mControler.registerListener(new BluetoothListener() {

            @Override
            public void onListenStart() {
                mView.showConnectingInfo();
            }

            @Override
            public void onListenTimeout() {
                mView.showConnectFailedInfo();
            }

            @Override
            public void onConnectStart(Device target) {
                mView.showConnectingInfo();
            }

            @Override
            public void onConnectSucceed(Device device) {
                mView.showConnectedInfo(device);
            }

            @Override
            public void onConnectFailed(Device device) {
                mView.showConnectFailedInfo();
            }

            @Override
            public void onConnectLost() {

            }

            @Override
            public void onDataSent(byte[] buffer) {

            }

            @Override
            public void onDataReceived(byte[] buffer, int byteRead) {

            }
        });
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void detach() {
        mControler.clearListener();
        mControler.reset();
    }

    @Override
    public void listen(int timeout) {
        mControler.listenInsecure(timeout);
    }

    @Override
    public void connect(Device device) {
        mControler.connect(device, false);
    }

    @Override
    public void sendData(byte[] out) {
        mControler.sendData(out);
    }
}
