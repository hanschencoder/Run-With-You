package com.hanschen.runwithyou.main.today;

import com.hanschen.runwithyou.base.BasePresenter;
import com.hanschen.runwithyou.base.BaseView;

/**
 * @author HansChen
 */
public class TodayContract {

    interface View extends BaseView<Presenter> {

        void onStepUpdateSuccess(int count);

        void onStepUpdateFailure();
    }

    interface Presenter extends BasePresenter {

        void loadStepCount();
    }
}
