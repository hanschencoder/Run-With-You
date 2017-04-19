package site.hanschen.runwithyou.ui.login;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = RegisterModule.class)
interface RegisterComponent {

    void inject(RegisterActivity activity);
}
