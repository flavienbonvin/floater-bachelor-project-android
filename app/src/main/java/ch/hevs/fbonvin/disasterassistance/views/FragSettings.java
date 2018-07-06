package ch.hevs.fbonvin.disasterassistance.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        Button button = mViewFragement.findViewById(R.id.btNetworkStatus);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActivityNetworkStatus.class);
                        startActivity(intent);
                    }
                }
        );

        return mViewFragement;
    }
}
