package site.hanschen.runwithyou.ui.home.doublerunner;


import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;
import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;

/**
 * @author HansChen
 */
class DoubleRunnerContract {

    interface View extends BaseView<Presenter> {

        void showConnectingInfo();

        void showConnectFailedInfo();

        void showConnectedInfo(Device device);
    }

    interface Presenter extends BasePresenter {
        /**
         * 开始监听蓝牙连接，无需配对
         *
         * @param timeout 超时时间
         */
        void listen(int timeout);

        /**
         * 开始连接蓝牙
         */
        void connect(Device device);

        /**
         * 发送数据
         */
        void sendData(byte[] out);
    }
}
