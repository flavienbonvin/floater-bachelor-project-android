package ch.hevs.fbonvin.disasterassistance;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.CommunicationManagement;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;
import ch.hevs.fbonvin.disasterassistance.utils.MessagesManagement;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;
import ch.hevs.fbonvin.disasterassistance.utils.PreferencesManagement;
import ch.hevs.fbonvin.disasterassistance.utils.interfaces.INearbyActivity;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMap;
import ch.hevs.fbonvin.disasterassistance.views.fragments.FragMessages;
import ch.hevs.fbonvin.disasterassistance.views.onBoards.ActivityOnBoard;
import ch.hevs.fbonvin.disasterassistance.views.settings.ActivityPreferences;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.CURRENT_DEVICE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.ESTABLISHED_ENDPOINTS;
import static ch.hevs.fbonvin.disasterassistance.Constant.FIRST_INSTALL;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGES_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGE_LIST;
import static ch.hevs.fbonvin.disasterassistance.Constant.FUSED_LOCATION_PROVIDER;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DISPLAYED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_EXPIRATION_DELAY;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_DELETED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_QUEUE_LOCATION;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.MIN_REFRESH_RATE_GPS;
import static ch.hevs.fbonvin.disasterassistance.Constant.NEARBY_MANAGEMENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.REFRESH_RATE_GPS;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;

public class MainActivity extends AppCompatActivity implements INearbyActivity{


    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

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

        Log.i(TAG, "onCreate: " + MESSAGE_EXPIRATION_DELAY);

        initButtons();
        initNearby();

        checkHighAccuracy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferencesManagement.saveMessages(this);
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferencesManagement.saveMessages(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferencesManagement.initPreferences(this);

        if(FIRST_INSTALL){
            Log.i(TAG, "onResume: first install, relaunch all service");
            initConstants();
            initNearby();
        }

        startLocationUpdates();
    }

    private void initConstants() {


        FUSED_LOCATION_PROVIDER = LocationServices.getFusedLocationProviderClient(this);
        configureLocation();

        //LocationManagement.getDeviceLocation();

        MESSAGES_RECEIVED = new ArrayList<>();
        MESSAGE_SENT = new ArrayList<>();
        MESSAGE_QUEUE = new ArrayList<>();
        MESSAGE_QUEUE_DELETED = new ArrayList<>();
        MESSAGES_DISPLAYED = new ArrayList<>();
        MESSAGE_QUEUE_LOCATION = new ArrayList<>();

        PreferencesManagement.retrieveMessages(this);
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

    private void configureLocation(){

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(REFRESH_RATE_GPS);
        mLocationRequest.setFastestInterval(MIN_REFRESH_RATE_GPS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i(TAG, "Main activity onSuccess: location settings satisfied");
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    CURRENT_DEVICE_LOCATION = location;
                    MessagesManagement.updateDisplayedMessagesList();
                    FRAG_MESSAGES_SENT.recalculateDistance();

                    //Send message that where queued because of no location stored
                    if(MESSAGE_QUEUE_LOCATION.size() > 0 && ESTABLISHED_ENDPOINTS.size() > 0){
                        Log.i(TAG, "MainActivity onLocationResult: send messages that did not had location " + MESSAGE_QUEUE_LOCATION.size());
                        for(Message m : MESSAGE_QUEUE_LOCATION){
                            m.setMessageLatitude(location.getLatitude());
                            m.setMessageLongitude(location.getLongitude());
                            m.updateExpirationDate();

                            CommunicationManagement.sendMessageListRecipient(new ArrayList<>(ESTABLISHED_ENDPOINTS.keySet()), m);
                        }
                    }

                }
            }
        };
    }

    private void startLocationUpdates(){
        MandatoryPermissionsHandling.checkPermission(this, CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
        FUSED_LOCATION_PROVIDER.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
    private void stopLocationUpdates() {
        FUSED_LOCATION_PROVIDER.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_action_settings:
                Intent intentSettings = new Intent(MainActivity.this, ActivityPreferences.class);
                startActivity(intentSettings);
                return true;
            case R.id.top_action_tutorial:
                Intent intentTutorial = new Intent(MainActivity.this, ActivityOnBoard.class);
                startActivity(intentTutorial);
                return true;
            case R.id.top_action_filter_date:
                MessagesManagement.OrderByDate(MESSAGES_DISPLAYED);
                FRAG_MESSAGE_LIST.updateDisplay();
                return true;
            case R.id.top_action_filter_title:
                MessagesManagement.OrderByTitle(MESSAGES_DISPLAYED);
                FRAG_MESSAGE_LIST.updateDisplay();
                return true;
            case R.id.top_action_filter_distance:
                MessagesManagement.OrderByDistance(MESSAGES_DISPLAYED);
                FRAG_MESSAGE_LIST.updateDisplay();
            case R.id.top_action_filter_category:
                MessagesManagement.OrderByCategory(MESSAGES_DISPLAYED);
                FRAG_MESSAGE_LIST.updateDisplay();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main_fragment, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void nearbyOk() {
        Snackbar.make(findViewById(R.id.snack_place), R.string.main_activity_google_nearby_launched, Snackbar.LENGTH_LONG).show();
    }
}
