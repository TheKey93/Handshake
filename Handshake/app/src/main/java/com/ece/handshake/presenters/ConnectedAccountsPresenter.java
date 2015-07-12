package com.ece.handshake.presenters;

import android.content.Context;
import android.content.res.Resources;

import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.model.db.AccountsDataSource;

import java.util.ArrayList;

public class ConnectedAccountsPresenter extends BasePresenter implements IConnectedAccountsInterface {
    private final static String CLASS_NAME = ConnectedAccountsPresenter.class.getClass().getSimpleName();

    private Context mContext;
    private Resources res;

    public ConnectedAccountsPresenter(Context context) {
        this.mContext = context;
        this.res = context.getResources();
    }

    @Override
    public ArrayList<SMAccount> getConnectedAccounts() {
        AccountsDataSource source = new AccountsDataSource(mContext);
        return source.getAccounts();
    }

    @Override
    public boolean removeConnectedAccount() {
        return false;
    }
}
