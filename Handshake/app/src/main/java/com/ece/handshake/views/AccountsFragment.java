package com.ece.handshake.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ece.handshake.R;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.presenters.BasePresenter;
import com.ece.handshake.presenters.ConnectedAccountsPresenter;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.ArrayList;


public class AccountsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private ConnectedAccountsPresenter mPresenter;

    public AccountsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ConnectedAccountsPresenter(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_account, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.accounts_recycler_view);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        AddFloatingActionButton addAccountButton = (AddFloatingActionButton) layout.findViewById(R.id.add_account_button);
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SMDialogFragment addAccountDialog = new SMDialogFragment();
                addAccountDialog.show(fm, "add_account_fragment");
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<SMAccount> accounts = mPresenter.getConnectedAccounts();

        mAdapter = new AccountsAdapter(accounts);
        mRecyclerView.setAdapter(mAdapter);
    }
}
