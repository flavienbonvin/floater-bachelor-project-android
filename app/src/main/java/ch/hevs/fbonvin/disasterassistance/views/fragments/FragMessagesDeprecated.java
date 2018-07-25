package ch.hevs.fbonvin.disasterassistance.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hevs.fbonvin.disasterassistance.R;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapter;
import ch.hevs.fbonvin.disasterassistance.adapter.RecyclerViewAdapterDeprecated;

import static ch.hevs.fbonvin.disasterassistance.Constant.MESSAGES_DEPRECATED;

public class FragMessagesDeprecated extends Fragment{

    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mViewFragment = inflater.inflate(R.layout.fragment_messages_deprecated, container, false);

        mRecyclerView = mViewFragment.findViewById(R.id.recycler_view_message_deprecated);
        mRecyclerViewAdapter = new RecyclerViewAdapterDeprecated(getActivity(), MESSAGES_DEPRECATED);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return mViewFragment;
    }

}
