package com.ece.handshake.model.data;

import android.net.Uri;

import java.io.Serializable;

public class PhoneContact extends Connection implements Serializable {
    private String mPhoneNumber;
    private String mEmailAddress;

    public PhoneContact() {}

    public PhoneContact (String firstName, String lastName, String phoneNumber, String emailAddress, Uri profilePic) {
        super(firstName, lastName, profilePic);
        mPhoneNumber = phoneNumber;
        mEmailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }
}
