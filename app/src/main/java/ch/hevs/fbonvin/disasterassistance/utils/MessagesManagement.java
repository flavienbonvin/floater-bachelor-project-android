package ch.hevs.fbonvin.disasterassistance.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGE_LIST;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DISPLAYED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_RADIUS_GEO_FENCING;

public class MessagesManagement {

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

                if(dist <= distMax){
                    Log.i(TAG, "MessageManagement updateDisplayedMessagesList: device is in the radius, added to the display " + m.getTitle());
                    MESSAGES_DISPLAYED.add(m);
                }
            }

            m.calculateProgress();
            if(m.getProgress() > 0 && !MESSAGES_DISPLAYED.contains(m)){
                Log.i(TAG, "MessageManagement updateDisplayedMessagesList: message still relevant " + m.getTitle() + " progress: " + m.getProgress());
                MESSAGES_DISPLAYED.add(m);

            }
            if(m.getProgress() < 0){
                Log.i(TAG, "MessageManagement updateDisplayedMessagesList: message not up to date, deleted: " + m.getTitle() + " progress: " + m.getProgress());
                toDelete.add(i);
            }
        }

        for (int i = MESSAGES_RECEIVED.size()-1; i >= 0; i--){
            if(toDelete.contains(i)){
                Message m = MESSAGES_RECEIVED.get(i);
                MESSAGES_RECEIVED.remove(m);
                MESSAGES_DISPLAYED.remove(m);
            }

        }

        FRAG_MESSAGE_LIST.updateDisplay();

    }

    public static void OrderByTitle(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getTitle().compareTo(message2.getTitle());
            }
        });
    }

    public static void OrderByDate(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getDateCreatedMillis().compareTo(message2.getDateCreatedMillis());
            }
        });
    }


    public static void OrderByDistance(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return Float.compare(message1.getDistance(), message2.getDistance());
            }
        });
    }

    public static void OrderByCategory(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getCategory().compareTo(message2.getCategory());
            }
        });
    }
}
