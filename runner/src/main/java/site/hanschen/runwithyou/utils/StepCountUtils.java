package site.hanschen.runwithyou.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * @author HansChen
 */
public class StepCountUtils {

    /**
     * @return 是否支持计步功能
     */
    public static boolean isStepCountSupport(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
               && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
               && sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null
               && sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null;
    }
}
