package com.ece.handshake.model.db;

import android.provider.BaseColumns;

public class PendingConnectionsContract {
    public PendingConnectionsContract() {}

    public static abstract class PendingConnectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "pending_connections";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PLATFORM_NAME = "platformName";
        public static final String LINK_URI = "profileLinkUri";
        public static final String PIC_URI = "profilePicUri";
        public static final String TOKEN = "accessToken";
        public static final String TIME_ENTERED = "dateAdded";
        public static final String IS_PHONE_CONTACT = "isPhoneContact";
    }
}