package com.ece.handshake.views;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ece.handshake.R;

public class ProfilesFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public ProfilesFragment() {

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
        mTabLayout = (TabLayout) layout.findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new ProfilesPagerAdapter(getActivity().getSupportFragmentManager(), 3));
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText("Social"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Business"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Other"));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return layout;
    }

}
