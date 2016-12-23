package site.hanschen.runwithyou.main.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import site.hanschen.runwithyou.R;


/**
 * @author HansChen
 */
public class MeFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.me_preferences);
        setPrefListeners();
    }

    private void setPrefListeners() {

        Preference updatePref = findPreference("pref_etc_check_update");
        updatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getApplicationContext(), "TODO:", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shensky711/Run-With-You/issues"));
                startActivity(intent);
                return true;
            }
        });
    }
}
