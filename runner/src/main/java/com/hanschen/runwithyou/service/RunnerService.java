package com.hanschen.runwithyou.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
            // TODO: 2016/11/16 for test
            return 65;
        }
    }
}
