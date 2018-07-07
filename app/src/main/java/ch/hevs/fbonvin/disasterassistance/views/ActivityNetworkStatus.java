package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
        //TODO: reconnect on back pressed
        //TODO: make the back button go to the setting fragment
    }

    private void setText(){

        TextView tvDiscovering = findViewById(R.id.tv_status_discover);
        TextView tvAdvertising = findViewById(R.id.tv_status_advertise);

        TextView tvPeersConnected = findViewById(R.id.tv_established_peer);
        TextView tvPeerConnecting = findViewById(R.id.tv_pending_peer);
        TextView tvPeerDiscovered = findViewById(R.id.tv_discovered_peer);

        ImageView imError = findViewById(R.id.im_status);

        if(NearbyManagement.ismIsDiscovering()){
            setTextWithColor(tvDiscovering, "Discovering OK", getResources().getColor(R.color.okColor));
        } else {
            setTextWithColor(tvDiscovering, "Discovering not OK", getResources().getColor(R.color.errorColor));
        }

        if(NearbyManagement.ismIsAdvertising()){
            setTextWithColor(tvAdvertising, "Advertising OK", getResources().getColor(R.color.okColor));
        } else {
            setTextWithColor(tvAdvertising, "Advertising not OK", getResources().getColor(R.color.errorColor));
        }

        setText(tvPeersConnected, tvPeersConnected.getText() + " " + String.valueOf(NearbyManagement.getEstablishedConnections().size()));
        setText(tvPeerConnecting, tvPeerConnecting.getText() + " " + String.valueOf(NearbyManagement.getPendingConnections().size()));
        setText(tvPeerDiscovered, tvPeerDiscovered.getText() + " " + String.valueOf(NearbyManagement.getDiscoveredEndpoint().size()));


        if ((NearbyManagement.ismIsDiscovering() || !NearbyManagement.ismIsAdvertising()) && NearbyManagement.ismIsConnecting()) {
            imError.setImageResource(R.drawable.ic_error_outline_black);
            imError.setColorFilter(this.getResources().getColor(R.color.errorColor));

            Snackbar.make(findViewById(android.R.id.content), "There is a problem with Google Nearby", Snackbar.LENGTH_LONG)
                    .setAction("More info", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: display help for fixing problems (Toggle bluetooth, reboot)
                        }
                    }).show();
        }
    }


    private void setText(TextView tv, String text){
        tv.setText(text);
    }

    private void setTextWithColor(TextView tv, String text, int color){
        tv.setText(text);
        tv.setTextColor(color);
    }
}
