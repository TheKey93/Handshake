package com.ece.handshake.presenters;


import com.ece.handshake.model.data.SMAccount;

import java.util.ArrayList;

public interface IProfilesPresenter {
    ArrayList<SMAccount> getConnectedAccounts();

    void togglePlatform(String platformName, int rowPosition, String profileName, boolean isEnabled);

    boolean isPlatformEnabledForProfile(String platform, String profileName);
}
