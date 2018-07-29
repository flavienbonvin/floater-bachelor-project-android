package ch.hevs.fbonvin.disasterassistance.utils;

import android.location.Location;

import java.math.BigDecimal;
import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;

public abstract class LocationManagement {

    /**
     * Get the distance between two points
     * @param provider name of the point to update
     * @param lat latitude do compare to
     * @param lng longitude to compare to
     * @return
     */
    public static float getDistance(String provider ,double lat, double lng){
        Location locationMessage = new Location(provider);
        locationMessage.setLongitude(lng);
        locationMessage.setLatitude(lat);

        float distance = locationMessage.distanceTo(CURRENT_DEVICE_LOCATION);

        BigDecimal bd = new BigDecimal(Float.toString(distance));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        return bd.floatValue();
    }

    /**
     * Update the distance of a list of message
     * @param messages list of message to recalculate the distance
     */
    public static void getDistance(ArrayList<Message> messages){

        for (int i = 0; i < messages.size(); i++){
            String provider = messages.get(i).getTitle();
            Double lat = messages.get(i).getMessageLatitude();
            Double lng = messages.get(i).getMessageLongitude();

            float dist = LocationManagement.getDistance(provider, lat, lng);

            messages.get(i).setDistance(dist);
        }
    }
}
