package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;
import ch.hevs.fbonvin.disasterassistance.utils.interfaces.INearbyActivity;

import static ch.hevs.fbonvin.disasterassistance.Constant.CONNECTING_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.DISCOVERED_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.ESTABLISHED_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class NearbyManagement {


    private static final Strategy mStrategy = Strategy.P2P_CLUSTER;

    //Store the state of the device
    private static boolean mIsConnecting = false;
    private static boolean mIsDiscovering = false;
    private static boolean mIsAdvertising = false;


    private final ConnectionsClient sConnectionsClient;
    private final String sAppID;
    private final String sPackageName;



    public NearbyManagement(ConnectionsClient connectionsClient, String appID, String packageName) {
        sConnectionsClient = connectionsClient;
        sAppID = appID;
        sPackageName = packageName;
    }

    /**
     * Start the discovery and advertising process
     * @param activity origin of the call
     */
    public void startNearby(Activity activity) {
        startAdvertising(sConnectionsClient, sAppID, sPackageName);
        startDiscovery(sConnectionsClient, sPackageName, (INearbyActivity) activity);
    }


    /**
     * Start the process of advertising
     * @param connectionsClient client of Nearby
     * @param appID unique ID of the app (UUID)
     * @param packageName name of the app (getPackageName)
     */
    private void startAdvertising(ConnectionsClient connectionsClient, final String appID, String packageName) {
        mIsAdvertising = true;

        connectionsClient.startAdvertising(
                appID,
                packageName,
                mConnectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(mStrategy).build())

                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "startAdvertising onSuccess");
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mIsAdvertising = false;
                                Log.w(TAG, "startAdvertising onFailure ", e);
                            }
                        }
                );
    }

    /**
     * Start the process of advertising
     * @param connectionsClient client of Nearby
     * @param packageName name of the app (getPackageName)
     * @param iNearbyActivity interface used to display a toast to the user once nearby is launched
     */
    private void startDiscovery(ConnectionsClient connectionsClient, String packageName, final INearbyActivity... iNearbyActivity) {
        mIsDiscovering = true;

        connectionsClient.startDiscovery(
                packageName,
                mEndpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(mStrategy).build())

                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "startDiscovery onSuccess");
                                //Used to display a toast on the first screen
                                if(iNearbyActivity.length == 1 &&  mIsAdvertising && mIsDiscovering){
                                    iNearbyActivity[0].nearbyOk();
                                }
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mIsDiscovering = false;
                                Log.w(TAG, "startDiscovery onFailure: ", e);
                            }
                        }
                );
    }

    /**
     * Stop the advertising process
     */
    private void stopAdvertising() {
        Log.i(TAG, "stopAdvertising");

        mIsAdvertising = false;
        sConnectionsClient.stopAdvertising();
    }

    /**
     * Stop the discovery process
     */
    private void stopDiscovery() {
        Log.i(TAG, "stopDiscovery");

        mIsDiscovering = false;
        sConnectionsClient.stopDiscovery();
    }

    /**
     * Reset nearby. Stop and relaunch the discovery and advertising
     */
    private void resetNearby() {
        Log.i(TAG, "resetNearby, stop and restart discovery and advertising");

        stopAdvertising();
        stopDiscovery();

        startAdvertising(sConnectionsClient, sAppID, sPackageName);
        startDiscovery(sConnectionsClient, sPackageName);
    }

    /**
     * Send the data to a list of recipient
     * @param sendTo list of device to contact
     * @param string message to send
     */
    public void sendDataAsByteListRecipient(ArrayList<String> sendTo, String string) {

        if (sendTo.size() > 0) {

            byte[] array = string.getBytes();
            Payload payload = Payload.fromBytes(array);


            sConnectionsClient.sendPayload(sendTo, payload)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(TAG, "onComplete: message sent to unique recipient");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "sendDataAsByte onFailure: ", e);
                        }
                    });

        }
    }

    /**
     * Send the data to a unique recipient
     * @param sendTo device to send to
     * @param string message to send
     */
    public void sendDataAsByteUniqueRecipient(String sendTo, String string){

        byte [] array = string.getBytes();
        Payload payload = Payload.fromBytes(array);

        sConnectionsClient.sendPayload(sendTo, payload)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "sendDataAsByte onFailure: ", e);
                        }
                    });
    }


    /**
     * Callbacks for Google Nearby
     */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull final String endpointId, @NonNull ConnectionInfo connectionInfo) {
                    Log.i(TAG, "ConnectionLifecycleCallback onConnectionInitiated: " + connectionInfo.getEndpointName());

                    Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                    CONNECTING_ENDPOINTS.put(endpointId, endpoint);

                    sConnectionsClient.acceptConnection(endpointId, mPayloadCallback)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i(TAG, "ConnectionLifecycleCallback, onConnectionInitiated onSuccess: " + endpointId);
                                        }
                                    }
                            )
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //TODO: Handle the STATUS_BLUETOOTH_ERROR, ask to restart bluetooth or phone if occurring often
                                            Log.e(TAG, "ConnectionLifecycleCallback, onConnectionInitiated onFailure: " + e.getMessage());
                                        }
                                    }
                            );
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution connectionResolution) {
                    Log.i(TAG, String.format("ConnectionLifecycleCallback onConnectionResult: %s %s",
                            endpointId, connectionResolution.getStatus().getStatusCode()));

                    mIsConnecting = false;

                    //Remove the endpoint of the pending connection
                    Endpoint endpoint = CONNECTING_ENDPOINTS.remove(endpointId);

                    //Restart the discovering if it has been stopped in EndpointDiscoveryCallback
                    if (!mIsDiscovering) {
                        startDiscovery(sConnectionsClient, sPackageName);
                    }

                    switch (connectionResolution.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.i(TAG, String.format("ConnectionLifecycleCallback onConnectionResult: success! %s",
                                    endpointId));

                            //If the connection succeeded, the endpoint is put in the connected set
                            ESTABLISHED_ENDPOINTS.put(endpoint.getId(), endpoint);

                            //Send all messages the new peer
                            CommunicationManagement.sendAllMessagesNewPeer(endpoint);
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            Log.w(TAG, String.format("ConnectionLifecycleCallback onConnectionResult %s, %s",
                                    connectionResolution.getStatus().getStatusMessage(), endpointId));
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            Log.e(TAG, String.format("ConnectionLifecycleCallback onConnectionResult %s, %s",
                                    connectionResolution.getStatus().getStatusMessage(), endpointId));
                            break;
                    }

                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    if (!ESTABLISHED_ENDPOINTS.containsKey(endpointId)) {
                        Log.w(TAG, "ConnectionLifecycleCallback onDisconnected: unknown endpoint disconnected " + endpointId);
                    }
                    Log.i(TAG, "ConnectionLifecycleCallback onDisconnected: " + endpointId);
                    ESTABLISHED_ENDPOINTS.remove(endpointId);
                    resetNearby();
                }
            };

    private final PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                    CommunicationManagement.receivePayload(payload);
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                }
            };

    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull final String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                    Log.i(TAG, "EndpointDiscoveryCallback onEndpointFound: " + discoveredEndpointInfo.getEndpointName());

                    if (!mIsConnecting && !ESTABLISHED_ENDPOINTS.containsKey(endpointId)) {
                        Log.i(TAG, "onEndpointFound: tries to connect");

                        //Stop the discovering to reduce STATUS_BLUETOOTH_ERROR during connection
                        stopDiscovery();

                        Endpoint endpoint = new Endpoint(endpointId, discoveredEndpointInfo.getEndpointName());

                        if (!DISCOVERED_ENDPOINTS.containsKey(endpointId)){
                            DISCOVERED_ENDPOINTS.put(endpointId, endpoint);
                        }
                        mIsConnecting = true;
                        sConnectionsClient.requestConnection(
                                sAppID,
                                endpointId,
                                mConnectionLifecycleCallback)

                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "EndpointDiscoveryCallback, onEndpointFound onSuccess: " + endpointId);
                                            }
                                        }
                                )
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                mIsConnecting = false;
                                                Log.w(TAG, "EndpointDiscoveryCallback, onEndpointFound onFailure: " + e.getMessage());

                                                startDiscovery(sConnectionsClient, sPackageName);
                                            }
                                        }
                                );
                    } else {
                        Log.i(TAG, "EndpointDiscoveryCallback onEndpointFound, already connected to this peer, or already connecting to another peer");
                        Log.i(TAG, "onEndpointFound: " + mIsConnecting);
                    }
                }

                @Override
                public void onEndpointLost(@NonNull String endpointId) {
                    Log.i(TAG, "EndpointDiscoveryCallback onEndpointLost: " + endpointId);
                }
            };

    public boolean ismIsConnecting() {
        return mIsConnecting;
    }

    public boolean ismIsDiscovering() {
        return mIsDiscovering;
    }

    public boolean ismIsAdvertising() {
        return mIsAdvertising;
    }

}
