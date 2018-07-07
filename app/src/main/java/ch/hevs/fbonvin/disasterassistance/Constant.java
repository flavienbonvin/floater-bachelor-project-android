package ch.hevs.fbonvin.disasterassistance;

import android.Manifest;

public class Constant {

    public static final String TAG = "DisasterRescue";

    /**
     * All constants related to the permissions
     */
    public static final String[] MANDATORY_PERMISSION =
            new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
    public static final int CODE_MANDATORY_PERMISSIONS = 1;

    /**
     * All constants related to the preferences
     */
    public static final String PREF_NAME = "ch.hevs.fbonvin.settings";
    public static final String PREF_NOT_SET = "NOT_SET";

    public static final String KEY_PREF_ID = "id";
    public static final String KEY_PREF_USERNAME = "username";

    public static String VALUE_PREF_USERNAME;
}
