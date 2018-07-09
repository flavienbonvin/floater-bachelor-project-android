package ch.hevs.fbonvin.disasterassistance;

import android.Manifest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.views.FragMessages;

/**
 * File regrouping all the constant needed for the project
 */
public class Constant {

    public static final String TAG = "DisasterRescue";


    /**
     * Fragments saved instead of recreated each time
     */
    public static final FragMessages FRAG_MESSAGE = new FragMessages();


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

    //All message that have been received by the device
    public static ArrayList<Message> MESSAGES_RECEIVED;
    //All messages send by the device
    public static ArrayList<Message> MESSAGE_SENT;
    //All messages the user wanted to send but there was no peers around
    public static ArrayList<Message> MESSAGE_QUEUE;

    //Discovered devices
    public static final Map<String, Endpoint> DISCOVERED_ENDPOINTS = new HashMap<>();
    //Device that have pending connection
    public static final Map<String, Endpoint> CONNECTING_ENDPOINTS = new HashMap<>();
    //Device we are currently connected to
    public static final Map<String, Endpoint> ESTABLISHED_ENDPOINTS = new HashMap<>();

    /**
     * All constants related to the preferences
     */
    public static final String PREF_NAME = "ch.hevs.fbonvin.settings";

    //Store all the messages received, sent and in queue
    public static final String PREF_NAME_MESSAGE_RECEIVED = "ch.hevs.fbonvin.message.received";
    public static final String PREF_NAME_MESSAGE_SENT = "ch.hevs.fbonvin.message.sent";
    public static final String PREF_NAME_MESSAGE_QUEUE = "ch.hevs.fbonvin.message.queue";
    public static final String PREF_KEY_MESSAGE_RECEIVED = "message_received";
    public static final String PREF_KEY_MESSAGE_SENT = "message_sent";
    public static final String PREF_KEY_MESSAGE_QUEUE = "message_queue";

    public static final String PREF_NOT_SET = "NOT_SET";

    public static final String KEY_PREF_ID = "id";
    public static final String KEY_PREF_USERNAME = "username";

    public static String VALUE_PREF_APPID;
    public static String VALUE_PREF_USERNAME;
}
