package ch.hevs.fbonvin.disasterassistance.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.IListRecyclerAdapter;
import ch.hevs.fbonvin.disasterassistance.utils.LocationManagement;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;

public class FragMessagesList  extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    private IListRecyclerAdapter mIListRecyclerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_messages_list, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message_list);
        mSwipeRefreshLayout = mViewFragment.findViewById(R.id.swipe_refresh_layout_list);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MESSAGES_RECEIVED);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        mIListRecyclerAdapter = mRecyclerViewAdapter;

        swipeAction();


        //TODO hide FAB on scroll
        FloatingActionButton fabAddMessage = mViewFragment.findViewById(R.id.fab_add_message_list);
        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySendMessage.class);
                startActivity(intent);
            }
        });


        return mViewFragment;
    }


    private void swipeAction(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LocationManagement.getDeviceLocation();
                if(MESSAGES_RECEIVED.size() > 0){
                    recalculateDistance();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void recalculateDistance(){

        for (int i = 0; i < MESSAGES_RECEIVED.size(); i++){

            String provider = MESSAGES_RECEIVED.get(i).getTitle();
            Double lat = MESSAGES_RECEIVED.get(i).getMessageLatitude();
            Double lng = MESSAGES_RECEIVED.get(i).getMessageLongitude();
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
        MESSAGES_RECEIVED.add(0, message);

        //TODO extract the scroll to the top of the list on a preference, might be nice addition
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
        MESSAGES_RECEIVED.remove(pos);
        mRecyclerViewAdapter.notifyItemRemoved(pos);
    }
}
