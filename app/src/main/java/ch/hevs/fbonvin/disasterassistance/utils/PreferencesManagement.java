package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.app.MediaRouteActionProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_KEY_MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME_MESSAGE_SENT;
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


    /**
     * Save all the ArrayList of messages to a separate SharedPreferences file
     * @param activity activity used to call the SharedPreferences
     */
    public static void saveMessages(Activity activity){
        SharedPreferences prefsReceived = activity.getSharedPreferences(PREF_NAME_MESSAGE_RECEIVED, Context.MODE_PRIVATE);
        SharedPreferences prefsSent = activity.getSharedPreferences(PREF_NAME_MESSAGE_SENT, Context.MODE_PRIVATE);
        SharedPreferences prefsQueue = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE, Context.MODE_PRIVATE);

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
    }

    /**
     * Retrieve all messages saved in SharedPreferences and place them in corresponding ArrayList
     * @param activity activity used to call the SharedPreferences
     */
    public static void retrieveMessages(Activity activity){
        SharedPreferences prefsReceived = activity.getSharedPreferences(PREF_NAME_MESSAGE_RECEIVED, Context.MODE_PRIVATE);
        SharedPreferences prefsSent = activity.getSharedPreferences(PREF_NAME_MESSAGE_SENT, Context.MODE_PRIVATE);
        SharedPreferences prefsQueue = activity.getSharedPreferences(PREF_NAME_MESSAGE_QUEUE, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = prefsReceived.getString(PREF_KEY_MESSAGE_RECEIVED, "");
        Type typeReceived = new TypeToken<ArrayList<Message>>(){}.getType();
        ArrayList<Message> tempReceived = gson.fromJson(json, typeReceived);

        json = prefsSent.getString(PREF_KEY_MESSAGE_SENT, "");
        Type typeSent = new TypeToken<ArrayList<Message>>(){}.getType();
        ArrayList<Message> tempSent = gson.fromJson(json, typeSent);

        json = prefsQueue.getString(PREF_KEY_MESSAGE_QUEUE, "");
        Type typeQueue = new TypeToken<ArrayList<Message>>(){}.getType();
        ArrayList<Message> tempQueue = gson.fromJson(json, typeQueue);

        //Ensure that the ArrayLists are not empty
        if(tempReceived != null && tempReceived.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGES_RECEIVED " + tempReceived.size());
            MESSAGES_RECEIVED.addAll(tempReceived);
        }
        if(tempSent != null && tempSent.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGE_SENT " + tempSent.size());
            MESSAGE_SENT.addAll(tempSent);
        }
        if(tempQueue != null && tempQueue.size() > 0) {
            Log.i(TAG, "retrieveMessages: MESSAGE_QUEUE " + tempQueue.size());
            MESSAGE_QUEUE.addAll(tempQueue);
        }
    }
}
