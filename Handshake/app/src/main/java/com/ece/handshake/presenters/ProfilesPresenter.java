package com.ece.handshake.presenters;

import android.content.Context;

import com.ece.handshake.helper.SharedPreferencesManager;
import com.ece.handshake.model.data.Connection;
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
    public void togglePlatform(final String platformName, final int rowPosition, String profileName, boolean isEnabled) {
        SharedPreferencesManager.togglePlatformInProfile(mContext, platformName, profileName, isEnabled);
        mView.togglePlatformToast(rowPosition, isEnabled);
    }

    @Override
    public boolean isPlatformEnabledForProfile(String platform, String profileName) {
        return SharedPreferencesManager.isPlatformEnabledForProfile(mContext, platform, profileName);
    }
}
