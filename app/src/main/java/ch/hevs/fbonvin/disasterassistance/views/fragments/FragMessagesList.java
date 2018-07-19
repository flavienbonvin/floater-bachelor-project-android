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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;
import ch.hevs.fbonvin.disasterassistance.utils.MessagesManagement;
import ch.hevs.fbonvin.disasterassistance.views.activities.ActivitySendMessage;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DISPLAYED;
import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;

public class FragMessagesList  extends Fragment{

    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_messages_list, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message_list);
        mSwipeRefreshLayout = mViewFragment.findViewById(R.id.swipe_refresh_layout_list);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MESSAGES_DISPLAYED);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        swipeAction();


        final FloatingActionButton fabAddMessage = mViewFragment.findViewById(R.id.fab_add_message_list);
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
                if(MESSAGES_DISPLAYED.size() > 0){
                    recalculateDistance();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void recalculateDistance() {

        MessagesManagement.updateDisplayedMessagesList();
        mRecyclerViewAdapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Change the RecyclerView and add new message in the first element of the list
     */
    public void updateMessages(){

        mRecyclerViewAdapter.notifyItemInserted(0);
        MessagesManagement.updateDisplayedMessagesList();
        updateDisplay();
    }

    public void updateDisplay(){
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }


    /**
     * Remove the item in the RecyclerView at the position given in parameters
     * @param pos item to delete
     */
    public void removeItem(int pos) {
        MESSAGES_RECEIVED.remove(pos);
        mRecyclerViewAdapter.notifyItemRemoved(pos);
    }


    private ArrayList<Message> dummyMessagers = new ArrayList<>();
    private void createDummyData(){
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "Z", getString(R.string.category_Victims), "a", 46.209671, 7.396210, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "X", getString(R.string.category_Caretaker), "b", 46.210258, 7.395685, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "Y", getString(R.string.category_Danger), "c", 46.209267, 7.396216, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "D", getString(R.string.category_Danger), "d", 46.209943, 7.397085, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "A", getString(R.string.category_Resources), "e", 46.210325, 7.396232, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "F", getString(R.string.category_Danger), "f", 46.209844, 7.395851, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "G", getString(R.string.category_Victims), "g", 46.210535, 7.395868, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "H", getString(R.string.category_Resources), "h", 46.210836, 7.395116, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "I", getString(R.string.category_Victims), "i", 46.210867, 7.394906, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "J", getString(R.string.category_Victims), "j", 46.211441, 7.395804, "new", new ArrayList<String>()));
        dummyMessagers.add(new Message(VALUE_PREF_APPID, "dummy", "dummy", "K", getString(R.string.category_Caretaker), "k", 46.211179, 7.396303, "new", new ArrayList<String>()));
    }
}
