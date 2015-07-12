package com.ece.handshake.presenters;


import com.ece.handshake.model.data.SMAccount;

import java.util.ArrayList;

public interface IConnectedAccountsInterface {
    ArrayList<SMAccount> getConnectedAccounts();

    boolean removeConnectedAccount();
}
