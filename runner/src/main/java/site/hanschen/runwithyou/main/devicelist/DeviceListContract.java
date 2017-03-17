package site.hanschen.runwithyou.main.devicelist;


import java.util.Set;

import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;
import site.hanschen.runwithyou.main.devicelist.bean.Device;

/**
 * @author HansChen
 */
class DeviceListContract {

    interface View extends BaseView<Presenter> {

        void showAllDevice(Set<Device> devices);

        void addNewDevice(Device device);

        void showDiscoveryStartInfo();

        void showDiscoveryFinishedInfo();
    }

    interface Presenter extends BasePresenter {

        void loadHistoryDevices();

        void loadPairedDevices();

        void discoveryDevices();

        void cancelDiscovery();
    }
}
