package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class OfflineMapManager {


    public static void getTile(Activity activity){

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);


        String baseURL = "";
        if(CURRENT_DEVICE_LOCATION != null){

            double lon = CURRENT_DEVICE_LOCATION.getLongitude();
            double lat = CURRENT_DEVICE_LOCATION.getLatitude();
            int zoom = 15;


            int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
            int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
            if (xtile < 0)
                xtile=0;
            if (xtile >= (1<<zoom))
                xtile=((1<<zoom)-1);
            if (ytile < 0)
                ytile=0;
            if (ytile >= (1<<zoom))
                ytile=((1<<zoom)-1);

            baseURL = "http://mt1.google.com/vt/lyrs=m&x=" + xtile + "&y=" + ytile+ "&z=" +  zoom;
        }


        Log.i(TAG, "getTile: " + baseURL);

    }

}
