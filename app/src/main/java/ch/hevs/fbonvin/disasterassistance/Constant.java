package ch.hevs.fbonvin.disasterassistance;

import android.Manifest;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMessagesList;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMessagesSent;

/**
 * File regrouping all the constant needed for the project
 */
public class Constant {

    public static final String TAG = "DisasterRescue";


    /**
     * MOST IMPORTANT CONSTANTS OF THE APP
     */
    public static final long MESSAGE_EXPIRATION_DELAY = 120000;      //IN MILLISECONDS (1 MINUTE) MIN*60*1000



    /**
     * Fragments saved instead of recreated each time
     */
    public static final FragMessagesList FRAG_MESSAGE_LIST = new FragMessagesList();
    public static final FragMessagesSent FRAG_MESSAGES_SENT = new FragMessagesSent();



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
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
    public static final int CODE_MANDATORY_PERMISSIONS = 1;



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
    //All messages the user wanted to delete but there was no peers around
    public static ArrayList<Message> MESSAGE_QUEUE_DELETED;
    //All messages that are without location and wanted to be sent
    public static  ArrayList<Message> MESSAGE_QUEUE_LOCATION;

    //All messages that are in the radius defined in the settings
    public static ArrayList<Message> MESSAGES_DISPLAYED;

    public static final String MESSAGE_STATUS_NEW = "new";
    public static final String MESSAGE_STATUS_DELETE = "delete";
    public static final String MESSAGE_STATUS_UPDATE = "update";

    //Headers used to identify messages type
    public static final String HEADER_MESSAGE = "message";
    public static final String HEADER_UPDATE_STATUS = "updateStatus";

    //
    public static final String UPDATE_MESSAGE_STATUS_OK = "ok";
    public static final String UPDATE_MESSAGE_STATUS_NON_OK = "nok";



    /**
     * All constants related to Google Nearby
     */
    public static NearbyManagement NEARBY_MANAGEMENT;
    //Discovered devices
    public static final Map<String, Endpoint> DISCOVERED_ENDPOINTS = new HashMap<>();
    //Device that have pending connection
    public static final Map<String, Endpoint> CONNECTING_ENDPOINTS = new HashMap<>();
    //Device we are currently connected to
    public static final Map<String, Endpoint> ESTABLISHED_ENDPOINTS = new HashMap<>();



    /**
     * All constants related to the position
     */
    public static FusedLocationProviderClient FUSED_LOCATION_PROVIDER;
    public static Location CURRENT_DEVICE_LOCATION = null;
    public static final double MIN_DISTANCE_DUPLICATE = 10.0;
    public static final int REFRESH_RATE_GPS = 30000;
    public static final int MIN_REFRESH_RATE_GPS = 15000;


    /**
     * All constants related to the preferences
     */
    public static boolean FIRST_INSTALL = false;

    //Store all the messages received, sent,  in queue and in queue deletion
    public static final String PREF_NAME_MESSAGE_RECEIVED = "ch.hevs.fbonvin.message.received";
    public static final String PREF_NAME_MESSAGE_SENT = "ch.hevs.fbonvin.message.sent";
    public static final String PREF_NAME_MESSAGE_QUEUE = "ch.hevs.fbonvin.message.queue";
    public static final String PREF_NAME_MESSAGE_QUEUE_DELETED = "ch.hevs.fbonvin.message.queue.deleted";
    public static final String PREF_NAME_MESSAGE_QUEUE_LOCATION = "ch.hevs.fbonvin.message.queue.location";

    public static final String PREF_KEY_MESSAGE_RECEIVED = "message_received";
    public static final String PREF_KEY_MESSAGE_SENT = "message_sent";
    public static final String PREF_KEY_MESSAGE_QUEUE = "message_queue";
    public static final String PREF_KEY_MESSAGE_QUEUE_DELETED = "message_queue_deleted";
    public static final String PREF_KEY_MESSAGE_QUEUE_LOCATION = "message_queue_location";

    public static final String PREF_NOT_SET = "NOT_SET";

    public static String VALUE_PREF_APPID;
    public static String VALUE_PREF_USERNAME;
    public static String VALUE_PREF_RADIUS_GEO_FENCING;
}
