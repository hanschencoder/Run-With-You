package site.hanschen.runwithyou.database.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.dagger.AppContext;

/**
 * @author HansChen
 */
public class SettingRepositoryImpl implements SettingRepository {

    private Context           mContext;
    private SharedPreferences mPreferences;

    @Inject
    public SettingRepositoryImpl(@AppContext Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getTargetStep() {
        return mPreferences.getInt(mContext.getResources().getString(R.string.pref_target_step), 8000);
    }

    @Override
    public boolean isForegroundService() {
        return mPreferences.getBoolean(mContext.getResources().getString(R.string.pref_memory_resident_foreground_service), true);
    }
}
