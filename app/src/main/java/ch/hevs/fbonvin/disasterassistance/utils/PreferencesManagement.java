package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public abstract class PreferencesManagement {



    public static void createIDFirstInstall(Activity activity, String prefName) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        String android_key = prefs.getString("id", PREF_NOT_SET);

        if (android_key.equals(PREF_NOT_SET)) {
            android_key = UUID.randomUUID().toString();

            Log.i(TAG, "PreferencesManagement createIDFirstInstall: " + android_key);
            prefs.edit().putString("id", android_key).apply();
        }
    }

    public static String getStringPref(Activity activity, String prefName, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        String pref = prefs.getString(key, PREF_NOT_SET);
        Log.i(TAG, String.format("getStringPref: retrieving %s, result: %s", key, pref));

        if (!pref.equals(PREF_NOT_SET)) {
            return pref;
        }
        return PREF_NOT_SET;
    }

    public static void saveStringPref(Activity activity, String prefName, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        prefs.edit().putString(key, value).apply();
    }
}
