package ch.hevs.fbonvin.disasterassistance.utils;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.*;

public abstract class CommunicationManagement {

    /**
     * Send any string received to all connected peers
     * @param string payload to send
     * @return boolean, true if there are connected peers
     */
    public static void sendDataAsByte(String string, ArrayList<String> sendTo) {
        NEARBY_MANAGEMENT.sendDataAsByte(string, sendTo);
    }

    public static void sendDataAsByteToOneDevice(String string, String sendTo){
        NEARBY_MANAGEMENT.sendDataAsByteToOneDevice(string, sendTo);
    }


    /**
     * Handle the payload received and forward them to correcting methods depending on the type
     * @param endpointID id of the endpoint that sent the data
     * @param payload is the data received
     */
    public static void handlePayload(String endpointID, Payload payload) {

        //TODO once the payload is received, check distance of message and do not display it if too close
        if (payload.getType() == Payload.Type.BYTES) {

            String payloadAsString = new String(payload.asBytes());

            payloadAsByte(payloadAsString);

            //forwardMessageToOtherPeers(endpointID, payloadAsString);
        } else {
            Log.e(TAG, "HandlePayload: not a byte", null);
        }
    }


    public static void sendAllMessagesNewPeer(Endpoint endpoint){
        Log.i(TAG, "sendAllMessagesNewPeer: " + endpoint.getName());

        ArrayList<Message> listMessage = new ArrayList();

        listMessage.addAll(checkRecipientMessages(MESSAGES_RECEIVED, endpoint));
        listMessage.addAll(checkRecipientMessages(MESSAGE_QUEUE, endpoint));
        listMessage.addAll(checkRecipientMessages(MESSAGE_SENT, endpoint));

        for (Message m : listMessage){
            sendDataAsByteToOneDevice(m.toString(), endpoint.getId());
        }
    }


    private static ArrayList<Message> checkRecipientMessages(ArrayList<Message> messages, Endpoint endpoint){

        ArrayList listMessage = new ArrayList();

        if(messages.size() > 0){

            for (Message m : messages){
                if(!m.getMessageSentTo().contains(endpoint.getName())){
                    m.getMessageSentTo().add(endpoint.getName());
                    listMessage.add(m);
                }
            }
        }
        return listMessage;
    }


    /**
     * Handle all the payload received as byte, create new message
     * @param payload data to handle
     */
    private static void payloadAsByte(String payload) {

        Log.i(TAG, "Received payloadAsByte: " + payload);

        Message m = getMessageFromString(payload);

        if (m != null){
            boolean flagEquals = false;

            for(Message message : MESSAGES_RECEIVED){

                //Check if the message that is received has already been received
                if(m.getDateCreatedMillis().equals(message.getDateCreatedMillis()) && m.getTitle().equals(message.getTitle())){

                    flagEquals = true;
                    Log.i(TAG, "payloadAsByte: " + m.getTitle() + "(new) equals " + message.getTitle());

                    //If that's the case it update the ArrayList of recipient to avoid duplicate data
                    for(String appID : m.getMessageSentTo()){
                        if(!message.getMessageSentTo().contains(appID)){
                            message.getMessageSentTo().add(appID);
                        }
                    }
                }
            }

            if(!flagEquals){
                FRAG_MESSAGE.updateDisplay(m);
            }
        }
    }


    private static Message getMessageFromString(String payload){

        Message message = null;

        try {
            message = Message.createFromPayload(payload);
        } catch (Exception e) {
            Log.w(TAG, "payloadAsByte: ", e);
        }
        return message;
    }
}
