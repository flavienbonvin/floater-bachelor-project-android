package ch.hevs.fbonvin.disasterassistance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.utils.INearbyActivity;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMap;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMessages;
import ch.hevs.fbonvin.disasterassistance.views.settings.ActivityPreferences;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.FUSED_LOCATION_PROVIDER;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.NEARBY_MANAGEMENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;

public class MainActivity extends AppCompatActivity implements INearbyActivity{


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
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PreferencesManagement.initPreferences(this);


        initConstants();


        //Handle the mandatory permissions of the application
        MandatoryPermissionsHandling.checkPermission(this, CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);

        initButtons();
        initNearby();

        checkHighAccuracy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferencesManagement.saveMessages(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferencesManagement.initPreferences(this);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragMessages()).commit();
    }

    private void initNearby() {
        //Configuration of Nearby

        //TODO NEARBY DO NOT START FIRST APP LAUNCH
        ConnectionsClient connectionsClient = Nearby.getConnectionsClient(this);
        NEARBY_MANAGEMENT = new NearbyManagement(connectionsClient, VALUE_PREF_APPID, getPackageName());
        NEARBY_MANAGEMENT.startNearby(this);
    }

    private void checkHighAccuracy() {
        try {
            if (Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {

                new AlertDialog.Builder(this)
                        .setTitle("You have to enable high accuracy")
                        .setMessage("Go to settings")
                        .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);

                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_action_settings:
                Intent intent = new Intent(MainActivity.this, ActivityPreferences.class);
                startActivity(intent);
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

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.Mandatory_permissions))
                            .setMessage(getString(R.string.Mandatory_permission_message))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MandatoryPermissionsHandling.checkPermission(
                                            MainActivity.this,
                                            CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
                                }
                            })
                            .create().show();
                }
            }
        }
    }

    @Override
    public void nearbyOk() {
        Snackbar.make(findViewById(R.id.snack_place), R.string.Google_nearby_launched, Snackbar.LENGTH_LONG).show();
    }
}
