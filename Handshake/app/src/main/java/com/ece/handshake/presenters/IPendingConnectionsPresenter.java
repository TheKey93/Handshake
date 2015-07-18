package com.ece.handshake.presenters;


import com.ece.handshake.model.data.Connection;

import java.util.ArrayList;

public interface IPendingConnectionsPresenter {
    ArrayList<Connection> getPendingConnections();

    boolean removePendingConnection();
}
