package ch.hevs.fbonvin.disasterassistance.utils;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.*;

public abstract class CommunicationManagement {

    /**
     * Send any string received to all connected peers
     * @param string payload to send
     * @return boolean, true if there are connected peers
     */
    public static boolean sendDataAsByte(String string) {
        return NEARBY_MANAGEMENT.sendDataAsByte(string);
    }


    /**
     * Handle the payload received and forward them to correcting methods depending on the type
     * @param endpointID id of the endpoint that sent the data
     * @param payload is the data received
     */
    public static void handlePayload(String endpointID, Payload payload) {
        if (payload.getType() == Payload.Type.BYTES) {
            payloadAsByte(payload);

            //TODO forward to all other connected peers
        } else {
            Log.e(TAG, "HandlePayload: not a byte", null);
        }

    }


    /**
     * Handle all the payload received as byte, create new message
     * @param payload data to handle
     */
    private static void payloadAsByte(Payload payload) {

        String payloadAsString = new String(payload.asBytes());

        Log.i(TAG, "Received payloadAsByte: " + payloadAsString);

        //TODO check if the payload is a correct message
        try {
            Message message = Message.createFromPayload(payloadAsString);
            FRAG_MESSAGE.updateDisplay(message);
        } catch (Exception e) {
            Log.w(TAG, "payloadAsByte: ", e);
        }

    }
}
