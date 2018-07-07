package ch.hevs.fbonvin.disasterassistance.utils;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.fbonvin.disasterassistance.models.Endpoint;

import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class NearbyManagement {


    private static final Strategy mStrategy = Strategy.P2P_CLUSTER;

    //Store the state of the device
    private static boolean mIsConnecting = false;
    private static boolean mIsDiscovering = false;
    private static boolean mIsAdvertising = false;

    //Discovered devices
    private static final Map<String, Endpoint> mDiscoveredEndpoint = new HashMap<>();
    //Device that have pending connection
    private static final Map<String, Endpoint> mPendingConnections = new HashMap<>();
    //Device we are currently connected to
    private static final Map<String, Endpoint> mEstablishedConnections = new HashMap<>();


    private final ConnectionsClient sConnectionsClient;
    private final String sAppID;
    private final String sPackageName;


    public NearbyManagement(ConnectionsClient connectionsClient, String appID, String packageName) {
        sConnectionsClient = connectionsClient;
        sAppID = appID;
        sPackageName = packageName;
    }


    public boolean startNearby() {

        startAdvertising(sConnectionsClient, sAppID, sPackageName);
        startDiscovery(sConnectionsClient, sAppID, sPackageName);

        return mIsAdvertising && mIsDiscovering;
    }

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
                                Log.i(TAG, "startAdvertising onSuccess, endpoint: " + appID);
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

    private void startDiscovery(ConnectionsClient connectionsClient, final String appID, String packageName) {
        mIsDiscovering = true;
        mDiscoveredEndpoint.clear();
        connectionsClient.startDiscovery(
                packageName,
                mEndpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(mStrategy).build())

                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "startDiscovery onSuccess: " + appID);
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

    private void stopAdvertising() {
        Log.i(TAG, "stopAdvertising");

        mIsAdvertising = false;
        sConnectionsClient.stopAdvertising();
    }

    private void stopDiscovery() {
        Log.i(TAG, "stopDiscovery");

        mIsDiscovering = false;
        sConnectionsClient.stopDiscovery();
    }

    private void resetNearby() {
        Log.i(TAG, "resetNearby, stop and restart discovery and advertising");

        stopAdvertising();
        stopDiscovery();

        startAdvertising(sConnectionsClient, sAppID, sPackageName);
        startDiscovery(sConnectionsClient, sAppID, sPackageName);
    }

    public boolean sendDataAsByte(String string) {

        Log.i(TAG, "sendDataAsByte: " + string);

        if (mEstablishedConnections.size() > 0) {
            byte[] array = string.getBytes();
            Payload payload = Payload.fromBytes(array);

            ArrayList<String> sendTo = new ArrayList<>(mEstablishedConnections.keySet());

            sConnectionsClient.sendPayload(sendTo, payload)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "sendDataAsByte onFailure: ", e);
                        }
                    });

            return true;
        }
        return false;
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
                    mPendingConnections.put(endpointId, endpoint);

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
                                            Log.w(TAG, "ConnectionLifecycleCallback, onConnectionInitiated onFailure: ", e);
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
                    Endpoint endpoint = mPendingConnections.remove(endpointId);

                    //Restart the discovering if it has been stopped in EndpointDiscoveryCallback
                    if (!mIsDiscovering) {
                        startDiscovery(sConnectionsClient, sAppID, sPackageName);
                    }

                    switch (connectionResolution.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.i(TAG, String.format("ConnectionLifecycleCallback onConnectionResult: success! %s",
                                    endpointId));
                            //If the connection succeeded, the endpoint is put in the connected set
                            mEstablishedConnections.put(endpoint.getId(), endpoint);

                            //TODO handle new peer connection
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
                    if (!mEstablishedConnections.containsKey(endpointId)) {
                        Log.w(TAG, "ConnectionLifecycleCallback onDisconnected: unknown endpoint disconnected " + endpointId);
                    }
                    Log.i(TAG, "ConnectionLifecycleCallback onDisconnected: " + endpointId);
                    mEstablishedConnections.remove(endpointId);
                }
            };

    private final PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                    String received = new String(payload.asBytes());
                    Log.i(TAG, "PayloadCallback onPayloadReceived: " + received);

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

                    if (!mIsConnecting && !mEstablishedConnections.containsKey(endpointId)) {
                        Log.i(TAG, "onEndpointFound: tries to connect");

                        //Stop the discovering to reduce STATUS_BLUETOOTH_ERROR during connection
                        stopDiscovery();

                        Endpoint endpoint = new Endpoint(endpointId, discoveredEndpointInfo.getEndpointName());
                        mDiscoveredEndpoint.put(endpointId, endpoint);

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
                                                Log.w(TAG, "EndpointDiscoveryCallback, onEndpointFound onFailure: ", e);

                                                //TODO: Handle the STATUS_BLUETOOTH_ERROR, ask to restart bluetooth or phone
                                                startDiscovery(sConnectionsClient, sAppID, sPackageName);
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

    public static Map<String, Endpoint> getDiscoveredEndpoint() {
        return mDiscoveredEndpoint;
    }

    public static Map<String, Endpoint> getPendingConnections() {
        return mPendingConnections;
    }

    public static Map<String, Endpoint> getEstablishedConnections() {
        return mEstablishedConnections;
    }

    public static boolean ismIsConnecting() {
        return mIsConnecting;
    }

    public static boolean ismIsDiscovering() {
        return mIsDiscovering;
    }

    public static boolean ismIsAdvertising() {
        return mIsAdvertising;
    }

    public static void onPayloadReceive() {

    }
}
