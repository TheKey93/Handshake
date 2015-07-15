package com.ece.handshake.presenters;


import com.ece.handshake.model.data.SMAccount;

import java.util.ArrayList;

public interface IProfilesPresenter {
    //TODO: Need to add parameter for different profiles

    ArrayList<SMAccount> getConnectedAccounts();

    void enablePlatform(String platformName, int rowPosition);

    void disablePlatform(String platformName, int rowPosition);

}
