package com.ece.handshake.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ece.handshake.R;
import com.ece.handshake.model.data.Connection;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.presenters.IPendingConnectionsView;
import com.ece.handshake.presenters.PendingConnectionsPresenter;

import java.util.ArrayList;


public class PendingConnectionsFragment extends Fragment implements IPendingConnectionsView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PendingConnectionsPresenter mPresenter;

    public PendingConnectionsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PendingConnectionsPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.pending_connections_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.pending_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Connection> accounts = mPresenter.getPendingConnections();

        mAdapter = new PendingConnectionsAdapter(mPresenter, accounts);
        mRecyclerView.setAdapter(mAdapter);
    }
}
