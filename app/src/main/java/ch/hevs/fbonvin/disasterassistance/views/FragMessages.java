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

import java.util.ArrayList;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.models.Message;

import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_APPID;
import static ch.hevs.fbonvin.disasterassistance.Constant.VALUE_PREF_USERNAME;

public class FragMessages extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_message, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), dummyMessage());

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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

    private ArrayList<Message> dummyMessage() {
        ArrayList<Message> data = new ArrayList<>();

        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Message 1", getString(R.string.category_victims), "Description 1 "));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Message 2", getString(R.string.category_danger), "Description 2"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Message 3", getString(R.string.category_resources), "Description 3"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Message 4", getString(R.string.category_danger), "Description 4"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Message 5", getString(R.string.category_resources), "Description 5"));

        return data;
    }
}
