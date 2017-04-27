package site.hanschen.runwithyou.ui.home.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.application.RunnerApplication;
import site.hanschen.runwithyou.database.repository.SettingRepository;
import site.hanschen.runwithyou.ui.WebViewActivity;
import site.hanschen.runwithyou.ui.about.AboutActivity;


/**
 * @author HansChen
 */
public class SettingPreferenceFragment extends PreferenceFragment {

    @Inject
    SettingRepository mSettingRepository;
    @Inject
    SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerPreferenceComponent.builder()
                                 .applicationComponent(RunnerApplication.getInstance().getAppComponent())
                                 .build()
                                 .inject(SettingPreferenceFragment.this);
        addPreferencesFromResource(R.xml.setting_preferences);
        mPreferences.registerOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferences.unregisterOnSharedPreferenceChangeListener(mOnPreferenceChangeListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPreferences();
    }

    SharedPreferences.OnSharedPreferenceChangeListener mOnPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case "pref_target_step":
                    int target = mSettingRepository.getTargetStep();
                    findPreference(key).setSummary(String.format(Locale.getDefault(), "每日运动目标：%d步", target));
                    break;
                default:
                    break;
            }
        }
    };

    private void initPreferences() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ((PreferenceGroup) findPreference(getString(R.string.pref_category_memory_resident))).removePreference(
                    findPreference(getString(R.string.pref_memory_resident_white_list)));
        }

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(mSettingRepository.getTargetStep());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer target) throws Exception {
                findPreference(getString(R.string.pref_target_step)).setSummary(String.format(Locale.getDefault(),
                                                                                              "每日运动目标：%d步",
                                                                                              target));
            }
        });
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
                WebViewActivity.startup(getActivity(), "https://github.com/shensky711/Run-With-You/issues", "GitHub Issues");
                break;
            case "pref_etc_about":
                startActivity(new Intent(getActivity(), AboutActivity.class));
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
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "oops...", Toast.LENGTH_SHORT).show();
        }
    }
}
