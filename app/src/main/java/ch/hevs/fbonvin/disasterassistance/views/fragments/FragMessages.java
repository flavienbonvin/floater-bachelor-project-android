package ch.hevs.fbonvin.disasterassistance.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.fbonvin.disasterassistance.R;

import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGES_SENT;
import static ch.hevs.fbonvin.disasterassistance.Constant.FRAG_MESSAGE_LIST;

public class FragMessages extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ViewPager viewPager = view.findViewById(R.id.view_pager_messages);
        setupViewPager(viewPager);

        TabLayout tabs = view.findViewById(R.id.tab_layout_messages);
        tabs.setupWithViewPager(viewPager);


        return view;
    }

    private void setupViewPager(ViewPager viewPager){

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(FRAG_MESSAGE_LIST, getString(R.string.adapter_tab_view_list_title));
        adapter.addFragment(FRAG_MESSAGES_SENT, getString(R.string.adapter_tab_view_list_sent));
        adapter.addFragment(new FragMessagesDeprecated(), "Deprecated");

        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
