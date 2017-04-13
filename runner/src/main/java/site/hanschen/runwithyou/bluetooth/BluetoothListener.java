package site.hanschen.runwithyou.bluetooth;

import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;

/**
 * @author HansChen
 */
public interface BluetoothListener {

    /**
     * 开始等待对方发起蓝牙连接
     */
    void onListenStart();

    /**
     * 等待连接超时
     */
    void onListenTimeout();

    /**
     * 开始连接目标设备
     */
    void onConnectStart(Device target);

    /**
     * 建立蓝牙连接
     *
     * @param device 对方的蓝牙设备
     */
    void onConnectSucceed(Device device);

    /**
     * 蓝牙连接失败, 主动发起蓝牙连接失败后调用
     */
    void onConnectFailed(Device device);

    /**
     * 蓝牙连接断开, 已建立蓝牙连接后, 由于某种原因导致连接断开后回调
     */
    void onConnectLost();

    /**
     * 成功发生数据后调用
     *
     * @param buffer 发送的数据内容
     */
    void onDataSent(byte[] buffer);

    /**
     * 接收到数据后调用
     *
     * @param buffer 接收的数据内容
     */
    void onDataReceived(byte[] buffer, int byteRead);
}
