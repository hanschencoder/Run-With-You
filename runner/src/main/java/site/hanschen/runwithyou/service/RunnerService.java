package site.hanschen.runwithyou.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * @author HansChen
 */
public class RunnerService extends Service {

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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RunnerManagerImpl();
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
