package ch.hevs.fbonvin.disasterassistance;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import ch.hevs.fbonvin.disasterassistance.utils.AlertDialogBuilder;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;
import ch.hevs.fbonvin.disasterassistance.views.FragMap;
import ch.hevs.fbonvin.disasterassistance.views.FragMessages;
import ch.hevs.fbonvin.disasterassistance.views.FragSettings;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.KEY_PREF_ID;
import static ch.hevs.fbonvin.disasterassistance.Constant.KEY_PREF_USERNAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;
import static ch.hevs.fbonvin.disasterassistance.Constant.NEARBY_MANAGEMENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public class MainActivity extends AppCompatActivity {


    private static boolean mHasMandatoryPermission = false;

    /**
     * Bottom navigation fragment switching management
     */
    private final BottomNavigationView.OnNavigationItemSelectedListener mNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_messages:
                            selectedFragment = new FragMessages();
                            break;
                        case R.id.nav_map:
                            selectedFragment = new FragMap();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new FragSettings();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    private BroadcastReceiver mBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initButtons();

        //Handle permissions and APP ID preferences
        mHasMandatoryPermission = MandatoryPermissionsHandling.checkPermission(this, CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);

        initPreferences();

        initNearby();

        //TODO: check if high accuracy is activated
    }

    private void initButtons() {
        //Initial configuration
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(mNavListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragMessages()).commit();
    }

    private void initNearby() {
        //Configuration of Nearby

        ConnectionsClient connectionsClient = Nearby.getConnectionsClient(this);
        NEARBY_MANAGEMENT = new NearbyManagement(connectionsClient, VALUE_PREF_APPID, getPackageName());

        if (NEARBY_MANAGEMENT.startNearby()) {
            Snackbar.make(findViewById(android.R.id.content), "You are connected", Snackbar.LENGTH_LONG)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }  //TODO what to do if there is a problem with nearby startup
    }

    private void initPreferences() {
        //Create the application ID of the application if not already created
        PreferencesManagement.createIDFirstInstall(this, PREF_NAME);
        VALUE_PREF_APPID = PreferencesManagement.getStringPref(this, PREF_NAME, KEY_PREF_ID);

        //Create the username
        VALUE_PREF_USERNAME = PreferencesManagement.getStringPref(this, PREF_NAME, KEY_PREF_USERNAME);
        if (VALUE_PREF_USERNAME.equals(PREF_NOT_SET)) {

            AlertDialogBuilder.showAlertDialogPositiveNegative(
                    this,
                    "No user name set",
                    "To help identify yourself, it is recommended to choose a username",
                    "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragSettings()).commit();
                        }
                    }, "Later", null);

        }
    }

    /**
     * Handle the mandatory permissions, if the access is not granted, the application restart
     *
     * @param requestCode  the request code received by the application, used to differentiate mandatory and optional permissions
     * @param permissions  string array containing all the permission required
     * @param grantResults array containing the result code of the permission check
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Handling of mandatory permissions of the application, the app do not work without them
        if (requestCode == CODE_MANDATORY_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {

                    //Create a dialog that will display an alert dialog promoting the user to accept
                    AlertDialogBuilder.showAlertDialogPositive(
                            MainActivity.this,
                            "Mandatory permission",
                            "This permission is mandatory",

                            "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MandatoryPermissionsHandling.checkPermission(
                                            MainActivity.this,
                                            CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
                                }
                            });
                }
            }
            mHasMandatoryPermission = true;
        }
    }
}
