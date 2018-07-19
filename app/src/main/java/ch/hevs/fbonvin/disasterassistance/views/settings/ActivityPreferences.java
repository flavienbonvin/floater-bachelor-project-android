package ch.hevs.fbonvin.disasterassistance.views.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivityAboutApplication;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivityNetworkStatus;

public class ActivityPreferences extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);


            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_user_name)));

            Preference btNetwork = findPreference(getString(R.string.key_pref_network_status));
            btNetwork.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(MainPreferenceFragment.this.getActivity(), ActivityNetworkStatus.class);
                    startActivity(intent);
                    return true;
                }
            });

            Preference btAbout = findPreference(getString(R.string.key_pref_about_app));
            btAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(MainPreferenceFragment.this.getActivity(), ActivityAboutApplication.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    private void getAPK(){



    }

    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String val = newValue.toString();

            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(val);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                preference.setSummary(val);
            } else if (preference instanceof EditTextPreference){
                preference.setSummary(val);
            }


            return true;
        }
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Make the back button of the action bar behave as the hardware button
     * @param item menu item pressed
     * @return true if ok, false if MenuItem not handled by method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }
}
