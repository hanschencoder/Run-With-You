package site.hanschen.runwithyou.bluetooth;

import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
public interface BluetoothControler {

    /**
     * 开始监听蓝牙连接，无需配对
     *
     * @param timeout 超时时间
     */
    void listenInsecure(int timeout);

    /**
     * 开始监听蓝牙连接，需要配对
     *
     * @param timeout 超时时间
     */
    void listenSecure(int timeout);

    /**
     * 开始连接蓝牙
     *
     * @param secure 是否需要配对
     */
    void connect(Device device, boolean secure);

    /**
     * 发送数据
     */
    void sendData(byte[] out);

    /**
     * 注册监听器
     */
    void registerListener(BluetoothListener listener);

    /**
     * 清除监听器
     */
    void clearListener();

    /**
     * 取消所有任务
     */
    void reset();
}
