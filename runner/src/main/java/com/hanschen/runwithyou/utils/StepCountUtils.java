package com.hanschen.runwithyou.utils;

import android.content.Context;
import android.content.pm.PackageManager;
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

        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER) && packageManager.hasSystemFeature(
                PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }
}
