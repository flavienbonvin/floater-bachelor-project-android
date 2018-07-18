package ch.hevs.fbonvin.disasterassistance.views.onBoards;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hevs.fbonvin.disasterassistance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragOnBoardScreen1 extends Fragment {


    public FragOnBoardScreen1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_board_screen1, container, false);
    }

}
