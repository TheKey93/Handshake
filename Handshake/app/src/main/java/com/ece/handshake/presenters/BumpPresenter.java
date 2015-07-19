package com.ece.handshake.presenters;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

import com.ece.handshake.helper.SharedPreferencesManager;
import com.ece.handshake.views.IBumpView;

public class BumpPresenter extends BasePresenter implements IBumpPresenter {
    private Context mContext;
    private IBumpView mView;

    public BumpPresenter(Context c, IBumpView view) {
        mContext = c;
        mView = view;
    }

    @Override
    public void isNfcEnabled() {
        NfcManager nfcManager = (NfcManager) mContext.getSystemService(Context.NFC_SERVICE);
        NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter();
        mView.setNfcStatus(nfcAdapter.isEnabled());
    }

    @Override
    public void setActiveProfile(String profile) {
        SharedPreferencesManager.setActiveNfcProfile(mContext, profile);
    }

    @Override
    public String getActiveProfile() {
        return SharedPreferencesManager.getActiveNfcProfile(mContext);
    }
}
