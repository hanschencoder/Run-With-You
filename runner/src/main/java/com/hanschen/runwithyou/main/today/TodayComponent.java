package com.hanschen.runwithyou.main.today;

import dagger.Component;

/**
 * @author HansChen
 */
@Component(modules = TodayPresenterModule.class)
public interface TodayComponent {

    void inject(TodayFragment fragment);
}
