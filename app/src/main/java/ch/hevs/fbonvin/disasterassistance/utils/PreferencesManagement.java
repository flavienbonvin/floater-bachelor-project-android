package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.Context;
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

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE_DELETED;
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

            //It the is true this means that this is the first application installation

        }

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

    public static boolean createIDFirstInstall(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String android_key = prefs.getString(activity.getString(R.string.key_pref_app_id), PREF_NOT_SET);

        if (android_key.equals(PREF_NOT_SET)) {
            android_key = UUID.randomUUID().toString();

            Log.i(TAG, "PreferencesManagement createIDFirstInstall: " + android_key);
            prefs.edit().putString(activity.getString(R.string.key_pref_app_id), android_key).apply();

            return true;
        }
        return false;
    }

    public static String getDefaultStringPref(Activity activity, String key, String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String pref = prefs.getString(key, defaultValue);

        Log.i(TAG, String.format("getDefaultStringPref: retrieving %s, result: %s", key, pref));

        return pref;
    }

    public static int getDefaultIntPref(Activity activity, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        int pref = prefs.getInt(key, -1);

        Log.i(TAG, String.format("getDefaultIntPref: retrieving %s, result: %s", key, pref));

        return pref;
    }

    public static void saveStringPref(Activity activity, String prefName, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);

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

        Gson gson = new Gson();

        String json = gson.toJson(MESSAGES_RECEIVED);
        prefsReceived.edit().putString(PREF_KEY_MESSAGE_RECEIVED, json).apply();
        Log.i(TAG, "saveMessages: MESSAGES_RECEIVED " + MESSAGES_RECEIVED.size());

        json = gson.toJson(MESSAGE_SENT);
        prefsSent.edit().putString(PREF_KEY_MESSAGE_SENT, json).apply();
        Log.i(TAG, "saveMessages: MESSAGE_SENT " + MESSAGE_SENT.size());

        json = gson.toJson(MESSAGE_QUEUE);
        prefsQueue.edit().putString(PREF_KEY_MESSAGE_QUEUE, json).apply();
        Log.i(TAG, "saveMessages: MESSAGE_QUEUE " + MESSAGE_QUEUE.size());

        json = gson.toJson(MESSAGE_QUEUE_DELETED);
        prefsQueueDeleted.edit().putString(PREF_KEY_MESSAGE_QUEUE_DELETED, json).apply();
        Log.i(TAG, "saveMessages: MESSAGE_QUEUE_DELETED " + MESSAGE_QUEUE_DELETED.size());
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

        Gson gson = new Gson();

        String json = prefsReceived.getString(PREF_KEY_MESSAGE_RECEIVED, "");
        Type typeReceived = new TypeToken<ArrayList<Message>>() {}.getType();
        ArrayList<Message> tempReceived = gson.fromJson(json, typeReceived);

        json = prefsSent.getString(PREF_KEY_MESSAGE_SENT, "");
        Type typeSent = new TypeToken<ArrayList<Message>>() {}.getType();
        ArrayList<Message> tempSent = gson.fromJson(json, typeSent);

        json = prefsQueue.getString(PREF_KEY_MESSAGE_QUEUE, "");
        Type typeQueue = new TypeToken<ArrayList<Message>>() {}.getType();
        ArrayList<Message> tempQueue = gson.fromJson(json, typeQueue);

        json = prefsQueueDeleted.getString(PREF_KEY_MESSAGE_QUEUE_DELETED, "");
        Type typeQueueDeleted = new TypeToken<ArrayList<Message>>() {}.getType();
        ArrayList<Message> tempQueueDeleted = gson.fromJson(json, typeQueueDeleted);

        //Ensure that the ArrayLists are not empty
        if (tempReceived != null && tempReceived.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGES_RECEIVED " + tempReceived.size());
            MESSAGES_RECEIVED.addAll(tempReceived);
        }
        if (tempSent != null && tempSent.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGE_SENT " + tempSent.size());
            MESSAGE_SENT.addAll(tempSent);
        }
        if (tempQueue != null && tempQueue.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGE_QUEUE " + tempQueue.size());
            MESSAGE_QUEUE.addAll(tempQueue);
        }
        if (tempQueueDeleted != null && tempQueueDeleted.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGE_QUEUE_DELETED " + tempQueueDeleted.size());
            MESSAGE_QUEUE_DELETED.addAll(tempQueueDeleted);
        }
    }
}
