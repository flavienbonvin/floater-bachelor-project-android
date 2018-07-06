package ch.hevs.fbonvin.disasterassistance.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public abstract class MandatoryPermissionsHandling {

    /**
     * Permission handling
     */
    public static boolean checkPermission(Activity activity, int permissionCode, String... permissions){
        if(!hasPermission(activity, permissions)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, permissionCode);
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * Check if the application has the mandatory permissiosn, if not return false
     * @param activity
     * @param permissions
     * @return
     */
    private static boolean hasPermission(Activity activity, String... permissions){
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
