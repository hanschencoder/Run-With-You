package site.hanschen.runwithyou.main.together;


import android.content.Intent;

import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;

/**
 * @author HansChen
 */
class TogetherContract {

    interface View extends BaseView<Presenter> {

        void startActivityForResult(Intent intent, int requestCode);

        void onBluetoothUnavailable();

        void onBluetoothEnable(boolean enable);

        void onBluetoothDiscoverable(boolean discoverable);
    }

    interface Presenter extends BasePresenter {

        void requestBluetoothEnable();

        /**
         * Makes this device discoverable
         */
        void requestBluetoothDiscoverable();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
