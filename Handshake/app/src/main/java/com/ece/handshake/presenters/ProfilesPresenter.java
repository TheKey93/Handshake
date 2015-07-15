package com.ece.handshake.presenters;

import android.content.Context;

import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.model.db.AccountsDataSource;
import com.ece.handshake.views.IProfilesView;

import java.util.ArrayList;

public class ProfilesPresenter implements IProfilesPresenter{
    private IProfilesView mView;
    private Context mContext;

    public ProfilesPresenter(Context context, IProfilesView profilesView) {
        this.mView = profilesView;
        this.mContext = context;
    }

    @Override
    public ArrayList<SMAccount> getConnectedAccounts() {
        AccountsDataSource source = new AccountsDataSource(mContext);
        return source.getConnectedAccounts();
    }

    @Override
    public void enablePlatform(String platformName, int rowPosition) {
        //TODO: Save settings to sharedpreferences under profile name
        //mView.highlightPlatformRowGreen();
    }

    @Override
    public void disablePlatform(String platformName, int rowPosition) {
        //mView.highlightPlatformRowRed();
    }
}
