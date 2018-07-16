package ch.hevs.fbonvin.disasterassistance.utils;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.FUSED_LOCATION_PROVIDER;
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

    private static void onSuccessLocation(Location location){
        CURRENT_DEVICE_LOCATION = location;
    }
}
