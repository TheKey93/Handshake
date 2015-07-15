package com.ece.handshake.model.data;

import android.net.Uri;

import java.io.Serializable;

public class Connection implements Serializable {
    private String mFirstName;
    private String mLastName;
    private Uri mProfilePicUri;
    private long mPendingConnectionAddedDate;

    public Connection() {}

    public Connection(String firstName, String lastName, Uri profilePic) {
        mFirstName = firstName;
        mLastName = lastName;
        mProfilePicUri = profilePic;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }


    public long getTime() {
        return mPendingConnectionAddedDate;
    }

    public void setTime(long mTime) {
        this.mPendingConnectionAddedDate = mTime;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public void setProfilePicUri(Uri mProfilePicUri) {
        this.mProfilePicUri = mProfilePicUri;
    }

    public Uri getProfilePicUri() {
        return mProfilePicUri;
    }

    public String getName() {
        return mFirstName + " " + mLastName;
    }
}
