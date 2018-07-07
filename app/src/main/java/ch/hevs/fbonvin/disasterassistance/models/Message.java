package ch.hevs.fbonvin.disasterassistance.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SEPARATOR;

public class Message implements Serializable {

    private String dateCreated;
    private String creatorAppId;
    private String senderAppID;
    private String creatorUserName;
    private String title;
    private String category;
    private String description;


    public Message() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreated = dateFormat.format(Calendar.getInstance().getTime());
    }


    public Message(String creatorAppId, String senderAppID, String creatorUserName, String title, String category, String description) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.US);
        dateCreated = dateFormat.format(Calendar.getInstance().getTime());

        this.creatorAppId = creatorAppId;
        this.senderAppID = senderAppID;
        this.creatorUserName = creatorUserName;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getString() {
        return
                dateCreated + MESSAGE_SEPARATOR +
                        creatorAppId + MESSAGE_SEPARATOR +
                        senderAppID + MESSAGE_SEPARATOR +
                        creatorUserName + MESSAGE_SEPARATOR +
                        title + MESSAGE_SEPARATOR +
                        category + MESSAGE_SEPARATOR +
                        description;
    }

    public Message createFromPayload(String payload) {
        Message received = new Message();

        String[] array = payload.split(MESSAGE_SEPARATOR);

        received.setDateCreated(array[0]);
        received.setCreatorAppId(array[1]);
        received.setSenderAppID(array[2]);
        received.setCreatorUserName(array[3]);
        received.setTitle(array[4]);
        received.setCategory(array[5]);
        received.setDescription(array[6]);

        return received;
    }


    /**
     * MESSAGE SETTER
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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
    public String getDateCreated() {
        return dateCreated;
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

