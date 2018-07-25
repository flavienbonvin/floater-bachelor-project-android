package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.views.onBoards.ActivityOnBoard;

import static ch.hevs.fbonvin.disasterassistance.Constant.FIRST_INSTALL;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DEPRECATED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_DEPRECATED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_DEPRECATED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_RADIUS_GEO_FENCING;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public abstract class PreferencesManagement {

    public static void initPreferences(Activity activity) {
        //Create the application ID of the application if not already created
        if (createIDFirstInstall(activity)) {
            FIRST_INSTALL = true;
            //It the is true this means that this is the first application installation
            Intent intent = new Intent(activity, ActivityOnBoard.class);
            activity.startActivity(intent);
        } else {
            VALUE_PREF_APPID = PreferencesManagement.getDefaultStringPref(
                    activity,
                    activity.getString(R.string.key_pref_app_id),
                    PREF_NOT_SET);

            VALUE_PREF_USERNAME = PreferencesManagement.getDefaultStringPref(
                    activity,
                    activity.getString(R.string.key_pref_user_name),
                    PREF_NOT_SET);

            VALUE_PREF_RADIUS_GEO_FENCING = PreferencesManagement.getDefaultStringPref(
                    activity,
                    activity.getString(R.string.key_pref_radius_geo_fencing),
                    activity.getString(R.string.default_value_radius_geo_fence));
        }
    }

    private static boolean createIDFirstInstall(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String android_key = prefs.getString(activity.getString(R.string.key_pref_app_id), PREF_NOT_SET);

        if (android_key.equals(PREF_NOT_SET)) {
            android_key = UUID.randomUUID().toString();

            prefs.edit().putString(activity.getString(R.string.key_pref_app_id), android_key).apply();
            return true;
        }
        return false;
    }

    public static String getDefaultStringPref(Activity activity, String key, String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String pref = prefs.getString(key, defaultValue);

        Log.i(TAG, String.format("PreferencesManagement getDefaultStringPref: retrieving %s, result: %s", key, pref));

        return pref;
    }

    public static void saveDefaultStringPref(Activity activity, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putString(key, value).apply();

    }


    /**
     * Save all the ArrayList of messages to a separate SharedPreferences file
     *
     * @param activity activity used to call the SharedPreferences
     */
    public static void saveMessages(Activity activity) {
        SharedPreferences prefsReceived = activity.getSharedPreferences(PREF_NAME_MESSAGE_RECEIVED, Context.MODE_PRIVATE);
        SharedPreferences prefsSent = activity.getSharedPreferences(PREF_NAME_MESSAGE_SENT, Context.MODE_PRIVATE);
        SharedPreferences prefsQueue = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE, Context.MODE_PRIVATE);
        SharedPreferences prefsQueueDeleted = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE_DELETED, Context.MODE_PRIVATE);
        SharedPreferences prefsQueueLocation = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences prefsDeprecated = activity.getSharedPreferences(PREF_NAME_MESSAGE_DEPRECATED, Context.MODE_PRIVATE);

        Gson gson = new Gson();


        String json = gson.toJson(MESSAGES_RECEIVED);
        prefsReceived.edit().putString(PREF_KEY_MESSAGE_RECEIVED, json).apply();

        json = gson.toJson(MESSAGE_SENT);
        prefsSent.edit().putString(PREF_KEY_MESSAGE_SENT, json).apply();

        json = gson.toJson(MESSAGE_QUEUE);
        prefsQueue.edit().putString(PREF_KEY_MESSAGE_QUEUE, json).apply();

        json = gson.toJson(MESSAGE_QUEUE_DELETED);
        prefsQueueDeleted.edit().putString(PREF_KEY_MESSAGE_QUEUE_DELETED, json).apply();

        json = gson.toJson(MESSAGE_QUEUE_LOCATION);
        prefsQueueLocation.edit().putString(PREF_KEY_MESSAGE_QUEUE_LOCATION, json).apply();

        json = gson.toJson(MESSAGES_DEPRECATED);
        prefsDeprecated.edit().putString(PREF_KEY_MESSAGE_DEPRECATED, json).apply();

        logState();
    }

    /**
     * Retrieve all messages saved in SharedPreferences and place them in corresponding ArrayList
     *
     * @param activity activity used to call the SharedPreferences
     */
    public static void retrieveMessages(Activity activity) {
        SharedPreferences prefsReceived = activity.getSharedPreferences(PREF_NAME_MESSAGE_RECEIVED, Context.MODE_PRIVATE);
        SharedPreferences prefsSent = activity.getSharedPreferences(PREF_NAME_MESSAGE_SENT, Context.MODE_PRIVATE);
        SharedPreferences prefsQueue = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE, Context.MODE_PRIVATE);
        SharedPreferences prefsQueueDeleted = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE_DELETED, Context.MODE_PRIVATE);
        SharedPreferences prefsQueueLocation = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences prefsDeprecated = activity.getSharedPreferences(PREF_NAME_MESSAGE_DEPRECATED, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Message>>() {
        }.getType();


        String json = prefsReceived.getString(PREF_KEY_MESSAGE_RECEIVED, "");
        ArrayList<Message> tempReceived = gson.fromJson(json, type);

        json = prefsSent.getString(PREF_KEY_MESSAGE_SENT, "");
        ArrayList<Message> tempSent = gson.fromJson(json, type);

        json = prefsQueue.getString(PREF_KEY_MESSAGE_QUEUE, "");
        ArrayList<Message> tempQueue = gson.fromJson(json, type);

        json = prefsQueueDeleted.getString(PREF_KEY_MESSAGE_QUEUE_DELETED, "");
        ArrayList<Message> tempQueueDeleted = gson.fromJson(json, type);

        json = prefsQueueLocation.getString(PREF_KEY_MESSAGE_QUEUE_LOCATION, "");
        ArrayList<Message> tempQueueLocation = gson.fromJson(json, type);

        json = prefsDeprecated.getString(PREF_KEY_MESSAGE_DEPRECATED, "");
        ArrayList<Message> tempDeprecated = gson.fromJson(json, type);


        //Ensure that the ArrayLists are not empty
        if (tempReceived != null && tempReceived.size() > 0) {
            MESSAGES_RECEIVED.addAll(tempReceived);
        }
        if (tempSent != null && tempSent.size() > 0) {
            MESSAGE_SENT.addAll(tempSent);
        }
        if (tempQueue != null && tempQueue.size() > 0) {
            MESSAGE_QUEUE.addAll(tempQueue);
        }
        if (tempQueueDeleted != null && tempQueueDeleted.size() > 0) {
            MESSAGE_QUEUE_DELETED.addAll(tempQueueDeleted);
        }
        if (tempQueueLocation != null && tempQueueLocation.size() > 0) {
            MESSAGE_QUEUE_LOCATION.addAll(tempQueueLocation);
        }
        if (tempDeprecated != null && tempDeprecated.size() > 0){
            MESSAGES_DEPRECATED.addAll(tempDeprecated);
        }

        logState();
    }

    private static void logState(){
        Log.i(TAG, "PreferencesManagement retrieveMessages: summary of messages (received, sent, queue, queue deleted, queue location, status update, deprecated) " +
                MESSAGES_RECEIVED.size() + " " +
                MESSAGE_SENT.size() + " " +
                MESSAGE_QUEUE.size() + " " +
                MESSAGE_QUEUE_DELETED.size() + " " +
                MESSAGE_QUEUE_LOCATION.size() + " " +
                MESSAGES_DEPRECATED.size());
    }
}
