package com.ece.handshake.model.data;

import android.net.Uri;

import java.io.Serializable;

public class SMAccount extends Connection implements Serializable{
    private String mPlatformName;
    private Uri mLinkUri;
    private String mAccessToken;

    public SMAccount() {
        super();
    }

    public SMAccount(String firstName, String lastName, String platformName, Uri linkUri, Uri profilePictureUri, String accessToken) {
        super(firstName, lastName, profilePictureUri);
        mPlatformName = platformName;
        mLinkUri = linkUri;
        mAccessToken = accessToken;
    }
    
    public String getPlatformName() {
        return mPlatformName;
    }

    public Uri getLinkUri() {
        return mLinkUri;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setPlatformName(String mPlatformName) {
        this.mPlatformName = mPlatformName;
    }

    public void setLinkUri(Uri mLinkUri) {
        this.mLinkUri = mLinkUri;
    }

    public void setAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }
}
