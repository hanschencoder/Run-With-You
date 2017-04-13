package site.hanschen.runwithyou.ui.home.doublerunner;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.ActivityScoped;

/**
 * @author HansChen
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = DoubleRunnerModule.class)
interface DoubleRunnerComponent {

    void inject(DoubleRunnerActivity activity);
}
