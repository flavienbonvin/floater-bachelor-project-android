package ch.hevs.fbonvin.disasterassistance;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.utils.AlertDialogBuilder;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMap;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMessages;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.FUSED_LOCATION_PROVIDER;
import static ch.hevs.fbonvin.disasterassistance.Constant.KEY_PREF_ID;
import static ch.hevs.fbonvin.disasterassistance.Constant.KEY_PREF_USERNAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.NEARBY_MANAGEMENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NAME;
import static ch.hevs.fbonvin.disasterassistance.Constant.PREF_NOT_SET;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public class MainActivity extends AppCompatActivity {


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
                            getSupportActionBar().setElevation(0);
                            selectedFragment = new FragMessages();
                            break;
                        case R.id.nav_map:
                            selectedFragment = new FragMap();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initConstants();

        //Handle the mandatory permissions of the application
         MandatoryPermissionsHandling.checkPermission(this, CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);

        initButtons();
        initPreferences();
        initNearby();

        //Retrieve all messages from the shared preference file
        PreferencesManagement.retrieveMessages(this);

        //TODO: check if high accuracy is activated, if not pop a message that redirect to the settings
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferencesManagement.saveMessages(this);
    }


    private void initConstants() {
        FUSED_LOCATION_PROVIDER = LocationServices.getFusedLocationProviderClient(this);
        LocationManagement.getDeviceLocation();

        MESSAGES_RECEIVED = new ArrayList<>();
        MESSAGE_SENT = new ArrayList<>();
        MESSAGE_QUEUE = new ArrayList<>();
        MESSAGE_QUEUE_DELETED = new ArrayList<>();

    }

    private void initButtons() {
        //Initial configuration
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(mNavListener);

        getSupportActionBar().setElevation(0);

        //TODO make preference, settings user can choose map or message list application startup
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragMessages()).commit();
    }

    private void initNearby() {
        //Configuration of Nearby


        //TODO NEARBY DO NOT START FIRST APP LAUNCH
        ConnectionsClient connectionsClient = Nearby.getConnectionsClient(this);
        NEARBY_MANAGEMENT = new NearbyManagement(connectionsClient, VALUE_PREF_APPID, getPackageName());
        //TODO FIX
        /*
        if (NEARBY_MANAGEMENT.startNearby()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.Google_nearby_launched, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }*/
    }

    private void initPreferences() {
        //Create the application ID of the application if not already created
        PreferencesManagement.createIDFirstInstall(this, PREF_NAME);
        VALUE_PREF_APPID = PreferencesManagement.getStringPref(this, PREF_NAME, KEY_PREF_ID);

        //Create the username
        VALUE_PREF_USERNAME = PreferencesManagement.getStringPref(this, PREF_NAME, KEY_PREF_USERNAME);
        if (VALUE_PREF_USERNAME.equals(PREF_NOT_SET)) {

            //TODO redo once the activity is added
            /*
            AlertDialogBuilder.showAlertDialogPositiveNegative(
                    this,
                    getString(R.string.no_user_name_set),
                    getString(R.string.message_no_username_set),
                    getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragSettings()).commit();
                        }
                    }, getString(R.string.later), null);
                    */

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.top_action_settings:
                //Intent intent = new Intent(MainActivity.this, ActivitySettings.class);
                //startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main_fragment, menu);

        return super.onCreateOptionsMenu(menu);
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
                            getString(R.string.Mandatory_permissions),
                            getString(R.string.Mandatory_permission_message),

                            getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MandatoryPermissionsHandling.checkPermission(
                                            MainActivity.this,
                                            CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
                                }
                            });
                }
            }
        }
    }
}
