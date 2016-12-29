package site.hanschen.runwithyou.database.repository;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.dagger.AppContext;

/**
 * @author HansChen
 */
public class SettingRepositoryImpl implements SettingRepository {

    private SharedPreferences mPreferences;
    private Context           mContext;

    @Inject
    public SettingRepositoryImpl(@AppContext Context context, SharedPreferences sharedPreferences) {
        this.mContext = context;
        this.mPreferences = sharedPreferences;
    }

    @Override
    public int getTargetStep() {
        return mPreferences.getInt(mContext.getResources().getString(R.string.pref_target_step), 8000);
    }

    @Override
    public boolean isForegroundService() {
        return mPreferences.getBoolean(mContext.getResources()
                                               .getString(R.string.pref_memory_resident_foreground_service), true);
    }
}
