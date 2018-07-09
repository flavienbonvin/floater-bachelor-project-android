package ch.hevs.fbonvin.disasterassistance;

import android.Manifest;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.views.FragMessages;

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


    public static NearbyManagement NEARBY_MANAGEMENT;

    /**
     * All constants related to the messages
     */
    public static final String MESSAGE_SEPARATOR = Character.toString((char)30);
    public static final ArrayList<Message> MESSAGES_RECEIVED = new ArrayList<>();

    /**
     * All constants related to the preferences
     */
    public static final String PREF_NAME = "ch.hevs.fbonvin.settings";
    public static final String PREF_NOT_SET = "NOT_SET";

    public static final String KEY_PREF_ID = "id";
    public static final String KEY_PREF_USERNAME = "username";

    public static String VALUE_PREF_APPID;
    public static String VALUE_PREF_USERNAME;
}
