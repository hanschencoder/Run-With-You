package site.hanschen.runwithyou.login;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = LoginModule.class)
interface LoginComponent {

    void inject(LoginActivity activity);
}
