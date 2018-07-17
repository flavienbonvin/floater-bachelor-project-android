package ch.hevs.fbonvin.disasterassistance.utils;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.math.BigDecimal;
import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGES_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.FUSED_LOCATION_PROVIDER;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public abstract class LocationManagement {

    public static void getDeviceLocation(){
        try {
            final Task locationTask = FUSED_LOCATION_PROVIDER.getLastLocation();

            locationTask.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() != null){
                        Log.i(TAG, "onComplete: location found");
                        onSuccessLocation((Location) locationTask.getResult());
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                    }
                }
            });
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: ", e);
        }
    }

    public static float getDistance(String provider ,double lat, double lng){


        Location locationMessage = new Location(provider);
        locationMessage.setLongitude(lng);
        locationMessage.setLatitude(lat);

        float distance = locationMessage.distanceTo(CURRENT_DEVICE_LOCATION);

        BigDecimal bd = new BigDecimal(Float.toString(distance));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);


        Log.i(TAG, "getDistance between two points, " + provider + " " + distance);

        return bd.floatValue();
    }

    private static void getDistance(ArrayList<Message> messages){

        for (int i = 0; i < messages.size(); i++){

            String provider = messages.get(i).getTitle();
            Double lat = messages.get(i).getMessageLatitude();
            Double lng = messages.get(i).getMessageLongitude();

            float dist = LocationManagement.getDistance(provider, lat, lng);

            messages.get(i).setDistance(dist);

        }
    }

    private static void onSuccessLocation(Location location){
        CURRENT_DEVICE_LOCATION = location;

        getDistance(MESSAGES_RECEIVED);
        getDistance(MESSAGE_SENT);

        FRAG_MESSAGES_SENT.distanceUpdated();
        FRAG_MESSAGES_SENT.distanceUpdated();
    }
}
