package site.hanschen.runwithyou.main.setting;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = SettingModule.class)
interface SettingComponent {

    void inject(SettingFragment fragment);
}
