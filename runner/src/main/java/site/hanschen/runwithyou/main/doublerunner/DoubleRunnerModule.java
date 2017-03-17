package site.hanschen.runwithyou.main.doublerunner;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import site.hanschen.runwithyou.bluetooth.BluetoothControler;
import site.hanschen.runwithyou.bluetooth.BluetoothControlerImpl;
import site.hanschen.runwithyou.dagger.AppContext;

/**
 * @author HansChen
 */
@Module
class DoubleRunnerModule {

    private final DoubleRunnerContract.View mView;

    DoubleRunnerModule(DoubleRunnerContract.View view) {
        mView = view;
    }

    @Provides
    DoubleRunnerContract.View provideDoubleRunnerView() {
        return mView;
    }

    @Provides
    BluetoothControler provideBluetoothControler(@AppContext Context context, @Nullable BluetoothAdapter adapter) {
        return new BluetoothControlerImpl(context, adapter);
    }
}
