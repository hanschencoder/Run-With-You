package site.hanschen.runwithyou.ui.home.together;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.FragmentScoped;

/**
 * @author HansChen
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = TogetherModule.class)
interface TogetherComponent {

    void inject(TogetherFragment fragment);
}
