package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;

public class ActivityNetworkStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_status);

        setText();

        //TODO: what to do if something is red and do not work
        //TODO: make the back button go to the setting fragment
    }

    private void setText(){

        TextView tvDiscovering = findViewById(R.id.tvStatusDiscover);
        TextView tvAdvertising = findViewById(R.id.tvStatusAdvertise);

        TextView tvPeersConnected = findViewById(R.id.tvDeviceConnected);
        TextView tvPeerConnecting = findViewById(R.id.tvDeviceConnecting);
        TextView tvPeerDiscovered = findViewById(R.id.tvDeviceDiscovered);

        if(NearbyManagement.ismIsDiscovering()){
            setTextWithColor(tvDiscovering, "Discovering OK", getResources().getColor(R.color.colorCategory2));
        } else {
            setTextWithColor(tvDiscovering, "Discovering not OK", getResources().getColor(R.color.colorCategory5));
        }

        if(NearbyManagement.ismIsAdvertising()){
            setTextWithColor(tvAdvertising, "Advertising OK", getResources().getColor(R.color.colorCategory2));
        } else {
            setTextWithColor(tvAdvertising, "Advertising not OK", getResources().getColor(R.color.colorCategory5));
        }

        setText(tvPeerConnecting, String.valueOf(NearbyManagement.getmEstablishedConnections().size()));
        setText(tvPeerConnecting, String.valueOf(NearbyManagement.getmPendingConnections().size()));
        setText(tvPeerDiscovered, String.valueOf(NearbyManagement.getmDiscoveredEndpoint().size()));
    }


    private void setText(TextView tv, String text){
        tv.setText(text);
    }

    private void setTextWithColor(TextView tv, String text, int color){
        tv.setText(text);
        tv.setTextColor(color);
    }
}
