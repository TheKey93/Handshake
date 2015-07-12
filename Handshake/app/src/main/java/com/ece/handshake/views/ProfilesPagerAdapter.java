package com.ece.handshake.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ProfilesPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public ProfilesPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                return null;
        }
        return new AccountsFragment();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
