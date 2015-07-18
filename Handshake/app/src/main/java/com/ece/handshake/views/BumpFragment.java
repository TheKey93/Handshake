package com.ece.handshake.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ece.handshake.R;


public class BumpFragment extends Fragment {
    public BumpFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.bump_fragment, container, false);

        return layout;
    }
}
