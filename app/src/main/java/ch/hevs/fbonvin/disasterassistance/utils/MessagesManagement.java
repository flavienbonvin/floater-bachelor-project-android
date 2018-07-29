package ch.hevs.fbonvin.disasterassistance.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGE_LIST;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DEPRECATED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DISPLAYED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_RADIUS_GEO_FENCING;

public class MessagesManagement {

    /**
     * Update the multiple elements displayed. Remove items that are not in the radius or that are expired
     */
    public static void updateDisplayedMessagesList(){
        MESSAGES_DISPLAYED.clear();


        if(CURRENT_DEVICE_LOCATION != null){
            LocationManagement.getDistance(MESSAGES_RECEIVED);
        }


        ArrayList<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < MESSAGES_RECEIVED.size(); i++){
            Message m = MESSAGES_RECEIVED.get(i);

            if(CURRENT_DEVICE_LOCATION != null){

                float dist = m.getDistance();
                int distMax = Integer.valueOf(VALUE_PREF_RADIUS_GEO_FENCING);

                m.calculateProgress();
                if(dist <= distMax && m.getProgress() > 0){
                    Log.i(TAG, "MessageManagement updateDisplayedMessagesList: device is in the radius, added to the display " + m.getTitle());
                    MESSAGES_DISPLAYED.add(m);
                } else if (m.getProgress() < 0){
                    Log.i(TAG, "MessageManagement updateDisplayedMessagesList: message not up to date, deleted: " + m.getTitle() + " progress: " + m.getProgress());
                    toDelete.add(i);
                }
            }
        }

        for (int i = MESSAGES_RECEIVED.size()-1; i >= 0; i--){
            if(toDelete.contains(i)){
                Message m = MESSAGES_RECEIVED.get(i);
                MESSAGES_RECEIVED.remove(m);
                MESSAGES_DISPLAYED.remove(m);
                MESSAGES_DEPRECATED.add(m);
            }

        }
        FRAG_MESSAGE_LIST.updateDisplay();

    }

    /**
     * Order a list of message by title
     * @param messages list of message to order
     */
    public static void OrderByTitle(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getTitle().compareTo(message2.getTitle());
            }
        });
    }

    /**
     * Order a list of message by date
     * @param messages list of message to order
     */
    public static void OrderByDate(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getDateCreatedMillis().compareTo(message2.getDateCreatedMillis());
            }
        });
    }

    /**
     * Order a list of message by distance
     * @param messages list of message to order
     */
    public static void OrderByDistance(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return Float.compare(message1.getDistance(), message2.getDistance());
            }
        });
    }

    /**
     * Order a list of message by category
     * @param messages list of message to order
     */
    public static void OrderByCategory(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getCategory().compareTo(message2.getCategory());
            }
        });
    }
}
