package ch.hevs.fbonvin.disasterassistance.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.KEY_PREF_USERNAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public class FragSettings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View mViewFragment = inflater.inflate(R.layout.fragment_settings, container, false);

        EditText etUsernameSetting = mViewFragment.findViewById(R.id.tv_settings_username);

        fillSettings(etUsernameSetting);
        saveSettings(mViewFragment);


        Button btNetStatus = mViewFragment.findViewById(R.id.bt_network_status);
        btNetStatus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActivityNetworkStatus.class);
                        startActivity(intent);
                    }
                }
        );

        return mViewFragment;
    }


    private void saveSettings(final View mViewFragment) {
        FloatingActionButton fabSave = mViewFragment.findViewById(R.id.fab_save_settings);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the value of the vie
                String username = ((EditText) mViewFragment.findViewById(R.id.tv_settings_username)).getText().toString();

                //Save settings to the preference file
                PreferencesManagement.saveStringPref(getActivity(), PREF_NAME, KEY_PREF_USERNAME, username);
                VALUE_PREF_USERNAME = PreferencesManagement.getStringPref(getActivity(), PREF_NAME, KEY_PREF_USERNAME);

                Toast.makeText(mViewFragment.getContext(), R.string.settings_saved, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillSettings(EditText editText) {
        String username = PreferencesManagement.getStringPref(getActivity(), PREF_NAME, KEY_PREF_USERNAME);
        if (username.equals(PREF_NOT_SET)) {
            editText.setHint(R.string.username_hint);
        } else {
            editText.setText(username);
        }
    }
}
