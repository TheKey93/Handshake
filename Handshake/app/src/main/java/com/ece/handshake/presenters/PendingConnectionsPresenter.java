package com.ece.handshake.presenters;


import android.content.Context;

import com.ece.handshake.model.data.Connection;
import com.ece.handshake.model.db.AccountsDataSource;

import java.util.ArrayList;

public class PendingConnectionsPresenter extends BasePresenter implements IPendingConnectionsPresenter{
    private Context mContext;

    public PendingConnectionsPresenter(Context context, IPendingConnectionsView view) {
        mContext = context;
    }


    @Override
    public ArrayList<Connection> getPendingConnections() {
        AccountsDataSource source = new AccountsDataSource(mContext);
        return source.getPendingConnections();
    }

    @Override
    public boolean removePendingConnection() {
        return false;
    }

    public Context getContext() {
        return mContext;
    }
}
