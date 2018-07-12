package ch.hevs.fbonvin.disasterassistance.utils;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;
import com.google.gson.Gson;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.*;

public abstract class CommunicationManagement {

    /**
     * Send a message to a list of recipient
     * @param sendTo list of recipient
     * @param message message to send
     */
    public static void sendMessageListRecipient(ArrayList<String> sendTo, Message message){
        Gson gson = new Gson();
        String[] content = new String[]{"message", message.toString()};

        NEARBY_MANAGEMENT.sendDataAsByteListRecipient(sendTo, gson.toJson(content));
    }


    /**
     * Send a message to a list of recipient
     * @param sendTo list of recipient
     * @param message message to send
     */
    private static void sendMessageUniqueRecipient(String sendTo, Message message){
        Gson gson = new Gson();
        String[] content = {HEADER_MESSAGE, message.toString()};

        NEARBY_MANAGEMENT.sendDataAsByteUniqueRecipient(sendTo, gson.toJson(content));
    }


    /**
     * Check and send all messages to a new connected peers
     * @param endpoint information about the new peer
     */
    public static void sendAllMessagesNewPeer(Endpoint endpoint){
        Log.i(TAG, "sendAllMessagesNewPeer: " + endpoint.getName());

        ArrayList<Message> listMessage = new ArrayList();

        listMessage.addAll(checkRecipientMessages(MESSAGES_RECEIVED, endpoint));
        listMessage.addAll(checkRecipientMessages(MESSAGE_QUEUE, endpoint));
        listMessage.addAll(checkRecipientMessages(MESSAGE_SENT, endpoint));

        for (Message m : listMessage){
            sendMessageUniqueRecipient(endpoint.getId(), m);
        }
    }


    /**
     * Handle the payload received and forward them to correcting methods depending on the type
     * @param payload is the data received
     */
    public static void receivePayload(Payload payload) {

        switch (payload.getType()){
            case Payload.Type.BYTES:
                Log.i(TAG, "receivePayload: payload as byte received");
                receivePayloadBytes(payload);
                break;
            case Payload.Type.FILE:
                break;
            case Payload.Type.STREAM:
            default:
                Log.e(TAG, "receivePayload: unknown payload type");
        }
    }


    /**
     * Handle the reception of a byte payload
     * @param payload data received
     */
    private static void receivePayloadBytes(Payload payload){

        Gson gson = new Gson();

        String s = new String(payload.asBytes());

        String[] payloadStringArray = gson.fromJson(s, String[].class);

        Log.i(TAG, "receivePayloadBytes: " + payloadStringArray[0]);

        switch (payloadStringArray[0].trim()){
            case HEADER_MESSAGE:
                Log.i(TAG, "receivePayloadBytes: new message received");
                receiveMessage(payloadStringArray[1]);
                break;

            default:
                Log.e(TAG, "receivePayloadBytes: unknown header type " + payloadStringArray[0]);
        }
    }


    /**
     * Handle all the payload received as byte, create new message
     * @param payload data to handle
     */
    private static void receiveMessage(String payload) {

        Log.i(TAG, "Received payloadAsByte: " + payload);

        Message m = getMessageFromString(payload);

        if(m != null){
            switch (m.getMessageStatus()){
                case MESSAGE_STATUS_NEW:
                    Log.i(TAG, "payloadAsByte: handle new message");
                    handleNewMessages(m);
                    break;
                case MESSAGE_STATUS_DELETE:
                    break;
                case MESSAGE_STATUS_UPDATE:
                    break;
                default:
                    Log.e(TAG, "payloadAsByte: unknown message status" + m.getMessageStatus());
            }

        }
    }


    /**
     * Handle the reception of a new message, avoid duplicate messages by checking the list of recipient
     * @param m message to handle
     */
    private static void handleNewMessages(Message m){

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

    /**
     * Check the content of messages list to know if it has already been send to the endpoint
     * @param messages ArrayList of messages to test
     * @param endpoint information about the peer
     * @return list of messages that can be send to the endpoint
     */
    private static ArrayList checkRecipientMessages(ArrayList<Message> messages, Endpoint endpoint){

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
     * Try to convert a string to a  message object
     * @param payload string to convert
     * @return message created
     */
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
