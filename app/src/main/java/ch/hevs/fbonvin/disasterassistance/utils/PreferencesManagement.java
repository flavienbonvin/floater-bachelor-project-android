package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public abstract class PreferencesManagement {

    private static final String NOT_SET = "NOT_SET";


    public static void createIDFirstInstall(Activity activity, String prefName) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        String android_key = prefs.getString("id", NOT_SET);

        if (android_key.equals(NOT_SET)) {
            android_key = UUID.randomUUID().toString();

            Log.i(TAG, "PreferencesManagement createIDFirstInstall: " + android_key);
            prefs.edit().putString("id", android_key).apply();
        }
    }

    public static String getStringPref(Activity activity, String prefName, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        String pref = prefs.getString(key, NOT_SET);

        if (!pref.equals(NOT_SET)) {
            return pref;
        }

        return null;
    }
}
