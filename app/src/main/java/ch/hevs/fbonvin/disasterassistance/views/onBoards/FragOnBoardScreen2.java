package ch.hevs.fbonvin.disasterassistance.views.onBoards;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.utils.MandatoryPermissionsHandling;

import static ch.hevs.fbonvin.disasterassistance.Constant.CODE_MANDATORY_PERMISSIONS;
import static ch.hevs.fbonvin.disasterassistance.Constant.MANDATORY_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragOnBoardScreen2 extends Fragment {


    public FragOnBoardScreen2() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_on_board_screen2, container, false);

        Button btPermission = view.findViewById(R.id.bt_on_board_permission);

        if(MandatoryPermissionsHandling.hasPermission(getActivity(), MANDATORY_PERMISSION)){
            btPermission.setText(R.string.on_board_activity_button_permission_required_granted);
            btPermission.setEnabled(false);
        }

        btPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MandatoryPermissionsHandling.checkPermission(getActivity(), CODE_MANDATORY_PERMISSIONS, MANDATORY_PERMISSION);
            }
        });


        return view;
    }
}
