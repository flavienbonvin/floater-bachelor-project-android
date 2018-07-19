package ch.hevs.fbonvin.disasterassistance.models;

import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_EXPIRATION_DELAY;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SEPARATOR;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class Message implements Serializable {


    /**
     * Variables related to the message
     */
    private String dateCreatedMillis;
    private String dateCreatedString;
    private String creatorAppId;
    private String senderAppID;
    private String creatorUserName;
    private String title;
    private String category;
    private String description;
    private Double messageLatitude;
    private Double messageLongitude;

    private String dateExpirationMillis;
    private int progress;

    /**
     * Variables related to the network information
     */
    private String messageStatus;
    private ArrayList<String> mMessageSentTo = new ArrayList<>(); //All endpointID where the message have been sent


    /**
     * Variables used for floating content
     */
    private float distance = -1;
    private boolean displayed = false;


    public Message() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreatedString = dateFormat.format(Calendar.getInstance().getTime());


        long timeMillis = System.currentTimeMillis();
        long dateExpiration = timeMillis + MESSAGE_EXPIRATION_DELAY;
        dateCreatedMillis = String.valueOf(timeMillis);
        dateExpirationMillis = String.valueOf(dateExpiration);

        progress = (int) ((timeMillis*100)/dateExpiration);

    }

    public Message(String creatorAppId, String senderAppID, String creatorUserName, String title, String category, String description, Double messageLatitude, Double messageLongitude, String messageStatus, ArrayList<String> messageSentTo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreatedString = dateFormat.format(Calendar.getInstance().getTime());

        long timeMillis = System.currentTimeMillis();
        long dateExpiration = timeMillis + MESSAGE_EXPIRATION_DELAY;
        dateCreatedMillis = String.valueOf(timeMillis);
        dateExpirationMillis = String.valueOf(dateExpiration);

        progress = (int) ((timeMillis*100)/dateExpiration);

        this.creatorAppId = creatorAppId;
        this.senderAppID = senderAppID;
        this.creatorUserName = creatorUserName;
        this.title = title;
        this.category = category;
        this.description = description;
        this.messageLatitude = messageLatitude;
        this.messageLongitude = messageLongitude;
        this.messageStatus = messageStatus;
        mMessageSentTo = messageSentTo;
    }

    @Override
    public String toString() {

        Gson gson = new Gson();
        String sentToArray = gson.toJson(mMessageSentTo);

        return
                //Information related to the message
                dateCreatedMillis + MESSAGE_SEPARATOR +       //0
                        dateExpirationMillis + MESSAGE_SEPARATOR + //1
                        dateCreatedString + MESSAGE_SEPARATOR +       //2
                        creatorAppId + MESSAGE_SEPARATOR +       //3
                        senderAppID + MESSAGE_SEPARATOR +       //4
                        creatorUserName + MESSAGE_SEPARATOR +       //5
                        title + MESSAGE_SEPARATOR +       //6
                        category + MESSAGE_SEPARATOR +       //7
                        description + MESSAGE_SEPARATOR +       //8
                        messageLatitude.toString() + MESSAGE_SEPARATOR +       //9
                        messageLongitude.toString() + MESSAGE_SEPARATOR +       //10

                        //Information related to the network
                        sentToArray + MESSAGE_SEPARATOR +       //11
                        messageStatus;
    }

    public static Message createFromPayload(String payload) {
        Message received = new Message();
        Gson gson = new Gson();

        String[] array = payload.split(MESSAGE_SEPARATOR);

        received.setDateCreatedMillis(array[0]);
        received.setDateExpirationMillis(array[1]);
        received.setDateCreatedString(array[2]);
        received.setCreatorAppId(array[3]);
        received.setSenderAppID(array[4]);
        received.setCreatorUserName(array[5]);
        received.setTitle(array[6]);
        received.setCategory(array[7]);
        received.setDescription(array[8]);
        received.setMessageLatitude(Double.valueOf(array[9]));
        received.setMessageLongitude(Double.valueOf(array[10]));

        received.setMessageSentTo(gson.fromJson(array[11], ArrayList.class));
        received.setMessageStatus(array[12]);

        Log.i(TAG, "Message createFromPayload: " + received.toString());

        return received;
    }

    public void calculateProgress(){
        long currentTime = System.currentTimeMillis();
        long dateCreation = Long.valueOf(dateCreatedMillis);
        long dateExpiration = Long.valueOf(dateExpirationMillis);

        long totalTime = dateExpiration - dateCreation;
        long remaining = dateExpiration - currentTime;


        progress = (int) (remaining*100/totalTime);

        Log.i(TAG, "Message calculateProgress: new progress calculated: " + TimeUnit.MILLISECONDS.toSeconds(remaining) + " seconds remaining: " + progress + "%");
    }

    public void updateExpirationDate(){
        long expiration = System.currentTimeMillis();

        dateExpirationMillis = String.valueOf(expiration + MESSAGE_EXPIRATION_DELAY);
    }

    public void extendExpirationDate(){
        long expiration = Long.valueOf(dateExpirationMillis);
        expiration += MESSAGE_EXPIRATION_DELAY;

        dateExpirationMillis = String.valueOf(expiration);
        calculateProgress();
    }

    public void decreaseExpirationDate(){
        long currentTimeMillis = System.currentTimeMillis();
        long expiration = Long.valueOf(dateExpirationMillis);

        long difference = expiration - currentTimeMillis;
        long remaining = currentTimeMillis + (difference/2);

        dateExpirationMillis = String.valueOf(remaining);
        calculateProgress();
    }

    /**
     * MESSAGE SETTER
     */
    public void setDateCreatedMillis(String dateCreatedMillis) {
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

    public void setMessageLatitude(Double messageLatitude) {
        this.messageLatitude = messageLatitude;
    }

    public void setMessageLongitude(Double messageLongitude) {
        this.messageLongitude = messageLongitude;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public void setDateExpirationMillis(String dateExpirationMillis) {
        this.dateExpirationMillis = dateExpirationMillis;
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

    public Double getMessageLatitude() {
        return messageLatitude;
    }

    public Double getMessageLongitude() {
        return messageLongitude;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public String getDateExpirationMillis() {
        return dateExpirationMillis;
    }

    public int getProgress() {
        return progress;
    }

    /**
     * GETTER and SETTER for network information
     */
    public ArrayList<String> getMessageSentTo() {

        return mMessageSentTo;
    }

    private void setMessageSentTo(ArrayList<String> messageSentTo) {
        mMessageSentTo = messageSentTo;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int retrieveMessageRecipient() {

        ArrayList<String> uniqueValues = new ArrayList<>();

        for (String s : mMessageSentTo) {
            if (!uniqueValues.contains(s) && !s.equals(creatorAppId)) {
                uniqueValues.add(s);
            }
        }

        Log.i(TAG, "retrieveMessageRecipient: " + Arrays.toString(uniqueValues.toArray()));

        return uniqueValues.size();
    }


    class MessageDateComparator implements Comparator<Message> {

        @Override
        public int compare(Message message1, Message message2) {
            return message1.getDateCreatedMillis().compareTo(message2.getDateCreatedMillis());
        }
    }

}

