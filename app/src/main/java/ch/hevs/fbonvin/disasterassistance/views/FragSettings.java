package ch.hevs.fbonvin.disasterassistance.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import ch.hevs.fbonvin.disasterassistance.MainActivity;
import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.utils.NearbyManagement;

public class FragSettings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragement = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView tvPeers = mViewFragement.findViewById(R.id.tvConnectedPeers);
        TextView tvPending = mViewFragement.findViewById(R.id.tvPendingPeers);
        String textPeers = "Connected peers: " + NearbyManagement.getmEstablishedConnections().size();
        String textPending = "Pending peers: " + NearbyManagement.getmPendingConnections().size();
        tvPeers.setText(textPeers);
        tvPending.setText(textPending);

        return mViewFragement;
    }
}
