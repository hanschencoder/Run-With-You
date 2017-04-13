package site.hanschen.runwithyou.ui.home.today;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = TodayPresenterModule.class)
interface TodayComponent {

    void inject(TodayFragment fragment);
}
