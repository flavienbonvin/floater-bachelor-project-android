package ch.hevs.fbonvin.disasterassistance.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivitySendMessage;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class FragMessagesSent extends Fragment {

    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_messages_sent, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message_sent);
        mSwipeRefreshLayout = mViewFragment.findViewById(R.id.swipe_refresh_layout_sent);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MESSAGE_SENT);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        swipeAction();


        final FloatingActionButton fabAddMessage = mViewFragment.findViewById(R.id.fab_add_message_sent);
        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySendMessage.class);
                startActivity(intent);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){
                    fabAddMessage.hide();
                } else {
                    fabAddMessage.show();
                }

            }
        });


        return mViewFragment;
    }

    private void swipeAction(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //LocationManagement.getDeviceLocation();
                if(MESSAGE_SENT.size() > 0){
                    recalculateDistance();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void recalculateDistance(){

        LocationManagement.getDistance(MESSAGE_SENT);

        ArrayList<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < MESSAGE_SENT.size(); i++){

            Message m = MESSAGE_SENT.get(i);

            m.calculateProgress();
            if(m.getProgress() < 0){
                toDelete.add(i);
            }
        }

        for (int i = MESSAGE_SENT.size()-1; i >= 0; i--){
            if(toDelete.contains(i)){
                Message m = MESSAGE_SENT.get(i);
                MESSAGE_SENT.remove(m);
            }
        }

        mRecyclerViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Change the RecyclerView and add new message in the first element of the list
     * @param message Message to add to the list
     */
    public void updateDisplay(Message message){

        Log.i(TAG, "updateDisplay: add message");
        MESSAGE_SENT.add(0, message);
        mRecyclerView.smoothScrollToPosition(0);

        recalculateDistance();
        updateDisplay();
    }

    public void updateDisplay(){
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void distanceUpdated(){
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Remove the item in the RecyclerView at the position given in parameters
     * @param pos item to delete
     */
    public void removeItem(int pos) {
        MESSAGE_SENT.remove(pos);
        mRecyclerViewAdapter.notifyItemRemoved(pos);
    }
}
