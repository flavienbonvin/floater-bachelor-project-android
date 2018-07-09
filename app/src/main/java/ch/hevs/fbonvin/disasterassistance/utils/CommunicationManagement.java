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
    public static boolean sendDataAsByte(String string, ArrayList<String> sendTo) {
        return NEARBY_MANAGEMENT.sendDataAsByte(string, sendTo);
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

            forwardMessageToOtherPeers(endpointID, payloadAsString);
        } else {
            Log.e(TAG, "HandlePayload: not a byte", null);
        }

    }


    public static void sendQueuedMessages(){

        if(MESSAGE_QUEUE.size() > 0){
            for(Message m : MESSAGE_QUEUE){
                sendDataAsByte(m.toString(), new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()));
            }
            MESSAGE_QUEUE.clear();
        }
    }


    private static void forwardMessageToOtherPeers(String endpointID, String payload){

        Map<String, Endpoint> temp = new HashMap<>(ESTABLISHED_ENDPOINTS);
        temp.remove(endpointID);

        sendDataAsByte(payload, new ArrayList<>(temp.keySet()));
    }


    /**
     * Handle all the payload received as byte, create new message
     * @param payload data to handle
     */
    private static void payloadAsByte(String payload) {

        Log.i(TAG, "Received payloadAsByte: " + payload);

        try {
            Message message = Message.createFromPayload(payload);
            FRAG_MESSAGE.updateDisplay(message);
        } catch (Exception e) {
            Log.w(TAG, "payloadAsByte: ", e);
        }
    }
}
