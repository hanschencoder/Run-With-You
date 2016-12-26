package site.hanschen.runwithyou.main.today;


import site.hanschen.runwithyou.base.BasePresenter;
import site.hanschen.runwithyou.base.BaseView;

/**
 * @author HansChen
 */
class TodayContract {

    interface View extends BaseView<Presenter> {

        void onStepLoadSuccess(int count);

        void onStepLoadFailed();
    }

    interface Presenter extends BasePresenter {

        void loadStepCount();
    }
}
