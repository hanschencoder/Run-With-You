package site.hanschen.runwithyou.main.devicelist;


import java.util.List;

import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
class DeviceListContract {

    interface View extends BaseView<Presenter> {

        void onDeviceLoaded(List<Device> devices);
    }

    interface Presenter extends BasePresenter {

        void loadHistoryDevices();

        void loadPairedDevices();

        void findNewDevices();

    }
}
