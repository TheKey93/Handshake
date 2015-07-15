package com.ece.handshake.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ece.handshake.R;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.presenters.ProfilesPresenter;

import java.util.ArrayList;

import io.karim.MaterialTabs;

public class ProfileTabFragment extends Fragment {
    private static int NUMBER_OF_TABS = 3;

    private MaterialTabs mTabs;
    private ViewPager mViewPager;

    public ProfileTabFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar actionBar = getActivity().getActionBar();
       // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profiles, container, false);

        mViewPager = (ViewPager) layout.findViewById(R.id.view_pager);
        mTabs = (MaterialTabs) layout.findViewById(R.id.tabs);

        mViewPager.setAdapter(new ProfilesPagerAdapter(getActivity().getSupportFragmentManager()));
        mTabs.setViewPager(mViewPager);
        return layout;
    }

    class ProfilesPagerAdapter extends FragmentPagerAdapter {
        private String[] tabs;

        public ProfilesPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.profile_tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            return ProfileFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return ProfileTabFragment.NUMBER_OF_TABS;
        }
    }

    public static class ProfileFragment extends Fragment implements IProfilesView{
        private static String POSITION = "position";

        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView.Adapter mAdapter;

        private ProfilesPresenter presenter;

        public static ProfileFragment getInstance(int position) {
            ProfileFragment fragment = new ProfileFragment();

            Bundle args = new Bundle();
            args.putInt(POSITION, position);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.single_profile, container, false);
            mRecyclerView = (RecyclerView) layout.findViewById(R.id.accounts_recycler_view);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            return layout;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            presenter = new ProfilesPresenter(getActivity(), this);

            Bundle bundle = getArguments();

            ArrayList<SMAccount> accounts = presenter.getConnectedAccounts();

            mAdapter = new ProfilesAdapter(getActivity(), accounts);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void highlightPlatformRowGreen(int rowPosition) {

        }

        @Override
        public void highlightPlatformRowRed(int rowPosition) {

        }
    }
}


