package site.hanschen.runwithyou.application;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import site.hanschen.runwithyou.service.RunnerManager;

/**
 * @author HansChen
 */
@Module
class ApplicationModule {

    private RunnerApplication mApp;

    ApplicationModule(RunnerApplication app) {
        this.mApp = app;
    }

    @Provides
    Context provideAppContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    RunnerManager provideRunnerManager() {
        return mApp.getRunnerManager();
    }
}
