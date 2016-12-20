package site.hanschen.runwithyou.startup;

import android.content.Context;
import android.content.SharedPreferences;

import site.hanschen.common.utils.AppUtils;


public class GuideTypeManager {

    private static final String SHARED_PREFERENCES_NAME = "first_start_pref";
    private static final String KEY_VERSION_CODE        = "version_code";

    private static final int DEFAULT_VERSION_CODE = -1;

    public static final int FIRST_START = 0;
    public static final int UPDATED     = 1;
    public static final int NORMAL      = 2;


    public static int getGuideTypeValue(Context context) {
        int lastVersionCode, curVersionCode;

        curVersionCode = AppUtils.getVersionCode(context);
        if (curVersionCode == 0) {
            return NORMAL;
        }

        lastVersionCode = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                                 .getInt(KEY_VERSION_CODE, DEFAULT_VERSION_CODE);
        if (lastVersionCode == DEFAULT_VERSION_CODE) {
            saveVersionCode(context, curVersionCode);
            return FIRST_START;
        } else if (curVersionCode > lastVersionCode) {
            saveVersionCode(context, curVersionCode);
            return UPDATED;
        } else {
            return NORMAL;
        }
    }

    private static void saveVersionCode(Context context, int versionCode) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KEY_VERSION_CODE, versionCode);
        editor.apply();
    }
}
