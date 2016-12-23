package site.hanschen.runwithyou.application;

import javax.inject.Singleton;

import dagger.Component;
import site.hanschen.runwithyou.service.RunnerManager;

/**
 * @author HansChen
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    RunnerManager getRunnerManager();
}
