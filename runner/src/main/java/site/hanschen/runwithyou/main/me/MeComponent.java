package site.hanschen.runwithyou.main.me;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = MeModule.class)
public interface MeComponent {

    void inject(MeFragment fragment);
}
