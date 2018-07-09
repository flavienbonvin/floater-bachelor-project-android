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

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_RECEIVED;
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
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), MESSAGES_RECEIVED);

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

        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Injured kid", getString(R.string.category_victims), "Kid with broken leg, needs assistance ASAP"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Gaz leak", getString(R.string.category_danger), "Strong gas smell in this area"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Water bottle", getString(R.string.category_resources), "Truck full of bottle arrived"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Collapsing wall", getString(R.string.category_danger), "The south wall of this house will fall is nothing is done"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Drugs", getString(R.string.category_resources), "We have few drugs we can share, some painkillers and antibiotics"));
        data.add(new Message(VALUE_PREF_APPID, VALUE_PREF_APPID, VALUE_PREF_USERNAME, "Nurse", getString(R.string.category_caretaker), "I am a nurse and will stay near the fountain all day, you can come if you need assistance of if you are not able to, I can come to you"));


        return data;
    }
}
