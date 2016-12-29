package site.hanschen.runwithyou.application;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;

import javax.inject.Singleton;

import dagger.Component;
import site.hanschen.runwithyou.dagger.AppContext;
import site.hanschen.runwithyou.database.repository.SettingRepository;
import site.hanschen.runwithyou.database.repository.StepRepository;
import site.hanschen.runwithyou.service.RunnerManager;

/**
 * @author HansChen
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    RunnerManager getRunnerManager();

    @AppContext
    Context getAppContext();

    SettingRepository getSettingRepository();

    StepRepository getStepRepository();

    SharedPreferences getDefaultSharedPreferences();

    SensorManager getSensorManager();

    NotificationManager getNotificationManager();
}
