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

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.IListRecyclerAdapter;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivitySendMessage;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGE_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.TAG;

public class FragMessagesSent extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private IListRecyclerAdapter mIListRecyclerAdapter;

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


        mIListRecyclerAdapter = mRecyclerViewAdapter;

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

                LocationManagement.getDeviceLocation();
                if(MESSAGE_SENT.size() > 0){
                    recalculateDistance();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void recalculateDistance(){

        for (int i = 0; i < MESSAGE_SENT.size(); i++){

            String provider = MESSAGE_SENT.get(i).getTitle();
            Double lat = MESSAGE_SENT.get(i).getMessageLatitude();
            Double lng = MESSAGE_SENT.get(i).getMessageLongitude();
            float dist = LocationManagement.getDistance(provider, lat, lng);

            mIListRecyclerAdapter.updateDistance(dist, i);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Change the RecyclerView and add new message in the first element of the list
     * @param message Message to add to the list
     */
    public void updateDisplay(Message message){
        Log.i(TAG, "updateDisplay: ss" + MESSAGE_SENT.size());
        MESSAGE_SENT.add(0, message);

        //Update the display of the recycler view and scroll to the top of it
        mRecyclerViewAdapter.notifyItemInserted(0);
        mRecyclerView.smoothScrollToPosition(0);
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
