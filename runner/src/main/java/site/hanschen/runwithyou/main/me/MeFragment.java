package site.hanschen.runwithyou.main.me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import site.hanschen.runwithyou.R;


/**
 * @author HansChen
 */
public class MeFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.me_preferences);
        initPreferences();
    }

    private void initPreferences() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ((PreferenceGroup) findPreference(getString(R.string.pref_category_memory_resident))).removePreference(findPreference(
                    getString(R.string.pref_memory_resident_white_list)));
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        handlePreferenceClick(preference);
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void handlePreferenceClick(Preference preference) {
        if (preference == null || preference.getKey() == null) {
            return;
        }
        switch (preference.getKey()) {
            case "pref_memory_resident_white_list":
                requestPowerWhiteList();
                break;
            case "pref_etc_check_update":
                // TODO: 2016/12/28
                break;
            case "pref_etc_issue":
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shensky711/Run-With-You/issues"));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void requestPowerWhiteList() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        String packageName = getActivity().getPackageName();
        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        Intent intent = new Intent();
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
        }
        startActivity(intent);
    }
}
