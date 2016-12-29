package site.hanschen.runwithyou.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.database.repository.SettingRepository;
import site.hanschen.runwithyou.main.MainActivity;

/**
 * @author HansChen
 */
public class RunnerService extends Service {

    private static final int NOTIFICATION_ID = 1;

    public static void bind(Context context, ServiceConnection conn) {
        Intent intent = new Intent(context, RunnerService.class);
        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public static void unbind(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    @Inject
    SettingRepository   mSettingRepository;
    @Inject
    SharedPreferences   mPreferences;
    @Inject
    SensorManager       mSensorManager;
    @Inject
    NotificationManager mNotificationManager;

    private Context mContext;
    private final    Handler                            mMainHandler = new Handler(Looper.getMainLooper());
    private final    RemoteCallbackList<RunnerCallback> mCallbacks   = new RemoteCallbackList<>();
    private volatile int                                mStepCount   = 0;
    private boolean                    mIsForegroundService;
    private NotificationCompat.Builder mNotificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        DaggerRunnerServiceComponent.builder()
                                    .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                                    .build()
                                    .inject(RunnerService.this);

        mPreferences.registerOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
        setForegroundState();
        setupSensor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferences.unregisterOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
        teardownSensor();
    }

    private void setupSensor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //Sensor stepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        //mSensorManager.registerListener(mSensorEventListener, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
        Sensor stepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(mSensorEventListener, stepCountSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void teardownSensor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RunnerManagerImpl();
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                mStepCount = (int) event.values[0];
                dispatchCallback(new CallbackRunnable<RunnerCallback>() {
                    @Override
                    public void run(RunnerCallback callback) throws RemoteException {
                        callback.onStepUpdate(mStepCount);
                    }
                });
                if (mIsForegroundService) {
                    mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener mOnPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "pref_memory_resident_foreground_service":
                    setForegroundState();
                    break;
                default:
                    break;
            }
        }
    };

    private void setForegroundState() {
        if (mSettingRepository.isForegroundService()) {
            startForeground();
        } else {
            stopForeground();
        }
    }

    private void startForeground() {
        mIsForegroundService = true;
        startForeground(NOTIFICATION_ID, getNotification());
    }

    private void stopForeground() {
        mIsForegroundService = false;
        stopForeground(true);
    }

    private Notification getNotification() {
        if (mNotificationBuilder == null) {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                                                                       .setContentTitle(getString(R.string.app_name))
                                                                       .setContentText(String.format(Locale.getDefault(),
                                                                                                     "当日步数: %d",
                                                                                                     mStepCount))
                                                                       .setContentIntent(pendingIntent)
                                                                       .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        return mNotificationBuilder.setContentText(String.format(Locale.getDefault(), "当日步数: %d", mStepCount)).build();
    }

    public final class RunnerManagerImpl extends RunnerManager.Stub {

        @Override
        public int getStepCount() throws RemoteException {
            return mStepCount;
        }

        @Override
        public void registerCallback(RunnerCallback callback) throws RemoteException {
            if (callback != null) {
                mCallbacks.register(callback);
            }
        }

        @Override
        public void unregisterCallback(RunnerCallback callback) throws RemoteException {
            if (callback != null) {
                mCallbacks.unregister(callback);
            }
        }
    }

    /**
     * dispatch {@link CallbackRunnable#run(Object)} on main thread. But consider {@link RunnerService} could be remote process
     * it still possible become non-main thread in client invoke
     */
    private void dispatchCallback(final CallbackRunnable<RunnerCallback> runnable) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                final int number = mCallbacks.beginBroadcast();
                for (int i = 0; i < number; i++) {
                    try {
                        runnable.run(mCallbacks.getBroadcastItem(i));
                    } catch (RemoteException ignore) {
                    }
                }
                mCallbacks.finishBroadcast();
            }
        });
    }

    interface CallbackRunnable<T> {

        void run(T callback) throws RemoteException;
    }
}
