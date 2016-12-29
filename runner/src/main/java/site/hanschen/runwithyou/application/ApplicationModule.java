package site.hanschen.runwithyou.application;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import site.hanschen.runwithyou.dagger.AppContext;
import site.hanschen.runwithyou.database.repository.SettingRepository;
import site.hanschen.runwithyou.database.repository.SettingRepositoryImpl;
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
    @AppContext
    Context provideAppContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    RunnerManager provideRunnerManager() {
        return mApp.getRunnerManager();
    }

    @Provides
    @Singleton
    SettingRepository provideSettingRepository(@AppContext Context context) {
        return new SettingRepositoryImpl(context);
    }

    @Provides
    @Singleton
    SharedPreferences provideDefaultSharedPreferences(@AppContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    SensorManager provideSensorManager(@AppContext Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(@AppContext Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
