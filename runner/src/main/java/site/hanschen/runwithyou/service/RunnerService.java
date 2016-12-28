package site.hanschen.runwithyou.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;

import site.hanschen.runwithyou.R;
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

    private Context mContext;
    private final    Handler                            mMainHandler = new Handler(Looper.getMainLooper());
    private final    RemoteCallbackList<RunnerCallback> mCallbacks   = new RemoteCallbackList<>();
    private volatile int                                mStepCount   = 65;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences.registerOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
        setForegroundState(mPreferences);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferences.unregisterOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RunnerManagerImpl();
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mOnPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "pref_memory_resident_foreground_service":
                    setForegroundState(sharedPreferences);
                    break;
                default:
                    break;
            }
        }
    };

    private void setForegroundState(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getBoolean("pref_memory_resident_foreground_service", true)) {
            startForeground();
        } else {
            stopForeground();
        }
    }

    private void startForeground() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                                                                        .setContentTitle(getString(R.string.app_name))
                                                                        .setContentText(String.format(Locale.getDefault(),
                                                                                                      "当日步数: %d",
                                                                                                      mStepCount))
                                                                        .setContentIntent(pendingIntent)
                                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                                        .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void stopForeground() {
        stopForeground(true);
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
