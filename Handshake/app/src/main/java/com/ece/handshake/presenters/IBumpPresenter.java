package com.ece.handshake.presenters;


public interface IBumpPresenter {
    void isNfcEnabled();

    void setActiveProfile(String profile);

    String getActiveProfile();
}
