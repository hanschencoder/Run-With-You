package site.hanschen.runwithyou.ui.home.today;


import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;

/**
 * @author HansChen
 */
class TodayContract {

    interface View extends BaseView<Presenter> {

        void showMaxCount(long max);

        void showCurrentStepCount(long count);

        void showStepLoadFailInfo();
    }

    interface Presenter extends BasePresenter {

        void loadStepCount();
    }
}
