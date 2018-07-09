package ch.hevs.fbonvin.disasterassistance.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;

public class FragMessages extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_message, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MESSAGES_RECEIVED);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        //TODO hide FAB on scroll
        FloatingActionButton fabAddMessage = mViewFragment.findViewById(R.id.fab_add_message);
        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySendMessage.class);
                startActivity(intent);
            }
        });


        return mViewFragment;
    }

    /**
     * Change the RecyclerView and add new message in the first element of the list
     * @param message Message to add to the list
     */
    public void updateDisplay(Message message){
        MESSAGES_RECEIVED.add(0, message);

        //TODO extract the scroll to the top of the list on a preference, might be nice addition
        //Update the display of the recycler view and scroll to the top of it
        mRecyclerViewAdapter.notifyItemInserted(0);
        mRecyclerView.smoothScrollToPosition(0);
    }
}
