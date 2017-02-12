package site.hanschen.runwithyou.service;

import dagger.Component;
import site.hanschen.runwithyou.application.ApplicationComponent;
import site.hanschen.runwithyou.dagger.ServiceScoped;

/**
 * @author HansChen
 */
@ServiceScoped
@Component(dependencies = ApplicationComponent.class)
interface RunnerServiceComponent {

    void inject(RunnerService service);
}
