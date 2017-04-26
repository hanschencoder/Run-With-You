package site.hanschen.runwithyou.ui.home.userinfo;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = UserInfoModule.class)
interface UserInfoComponent {

    void inject(UserInfoActivity activity);
}
