package ch.hevs.fbonvin.disasterassistance.models;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static ch.hevs.fbonvin.disasterassistance.Constant.*;

public class Message implements Serializable {

    private String dateCreatedMillis;
    private String dateCreatedString;
    private String creatorAppId;
    private String senderAppID;
    private String creatorUserName;
    private String title;
    private String category;
    private String description;



    public Message() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreatedString = dateFormat.format(Calendar.getInstance().getTime());

        dateCreatedMillis = String.valueOf(System.currentTimeMillis());
    }


    public Message(String creatorAppId, String senderAppID, String creatorUserName, String title, String category, String description) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreatedString = dateFormat.format(Calendar.getInstance().getTime());

        dateCreatedMillis = String.valueOf(System.currentTimeMillis());

        this.creatorAppId = creatorAppId;
        this.senderAppID = senderAppID;
        this.creatorUserName = creatorUserName;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return
                dateCreatedMillis + MESSAGE_SEPARATOR +
                dateCreatedString   + MESSAGE_SEPARATOR +
                creatorAppId        + MESSAGE_SEPARATOR +
                senderAppID         + MESSAGE_SEPARATOR +
                creatorUserName     + MESSAGE_SEPARATOR +
                title               + MESSAGE_SEPARATOR +
                category            + MESSAGE_SEPARATOR +
                description;
    }

    public static Message createFromPayload(String payload) {
        Message received = new Message();

        String[] array = payload.split(MESSAGE_SEPARATOR);

        received.setDateCreatedMillis(array[0]);
        received.setDateCreatedString(array[1]);
        received.setCreatorAppId(array[2]);
        received.setSenderAppID(array[3]);
        received.setCreatorUserName(array[4]);
        received.setTitle(array[5]);
        received.setCategory(array[6]);
        received.setDescription(array[7]);

        Log.i(TAG, "Successfully created a message from payload: " + received.toString());

        return received;
    }


    /**
     * MESSAGE SETTER
     */
    private void setDateCreatedMillis(String dateCreatedMillis) {
        this.dateCreatedMillis = dateCreatedMillis;
    }

    private void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }
    public void setCreatorAppId(String creatorAppId) {
        this.creatorAppId = creatorAppId;
    }

    public void setSenderAppID(String senderAppID) {
        this.senderAppID = senderAppID;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    /**
     * MESSAGE GETTER
     */
    public String getDateCreatedMillis() {
        return dateCreatedMillis;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }
    public String getCreatorAppId() {
        return creatorAppId;
    }

    public String getSenderAppID() {
        return senderAppID;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

