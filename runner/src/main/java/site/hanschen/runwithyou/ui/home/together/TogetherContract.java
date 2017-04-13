package site.hanschen.runwithyou.ui.home.together;


import android.content.Intent;

import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;

/**
 * @author HansChen
 */
class TogetherContract {

    interface View extends BaseView<Presenter> {

        void startActivityForResult(Intent intent, int requestCode);

        /**
         * 显示蓝牙不可用的提示
         */
        void showBTUnavailable();

        /**
         * 显示蓝牙打开/关闭的提示
         */
        void showBTSwitch(boolean enable);

        /**
         * 显示用户拒绝蓝牙可发现信息
         */
        void showDiscoverRefuseInfo();

        /**
         * 跳转主动连接界面
         */
        void showConnectDeviceInfo();

        /**
         * 跳转被动连接界面
         */
        void showDiscoverableInfo();
    }

    interface Presenter extends BasePresenter {

        /**
         * 请求打开蓝牙
         */
        void requestEnable();

        /**
         * 请求主动连接
         */
        void connectDevice();

        /**
         * 请求被动连接
         */
        void requestDiscoverable();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
