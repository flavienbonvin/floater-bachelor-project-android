package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class OfflineMapManager {

    /**
     * Experimentation, converting a coordinate latlng to a x y and z tile used to retrieve images from Google Maps
     * @param activity
     */
    public static void getTile(Activity activity){

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);


        String baseURL = "";
        if(CURRENT_DEVICE_LOCATION != null){

            double lon = CURRENT_DEVICE_LOCATION.getLongitude();
            double lat = CURRENT_DEVICE_LOCATION.getLatitude();
            int zoom = 15;


            int xTile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
            int yTile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
            if (xTile < 0)
                xTile=0;
            if (xTile >= (1<<zoom))
                xTile=((1<<zoom)-1);
            if (yTile < 0)
                yTile=0;
            if (yTile >= (1<<zoom))
                yTile=((1<<zoom)-1);
        }


        Log.i(TAG, "getTile: " + baseURL);

    }

}
