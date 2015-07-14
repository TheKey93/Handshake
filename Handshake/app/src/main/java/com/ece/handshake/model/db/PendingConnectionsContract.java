package com.ece.handshake.model.db;

import android.provider.BaseColumns;

public class PendingConnectionsContract {
    public PendingConnectionsContract() {}

    public static abstract class PendingConnectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "pending_connections";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PLATFORM_NAME = "platformName";
        public static final String COLUMN_LINK_URI = "profileLinkUri";
        public static final String COLUMN_PIC_URI = "profilePicUri";
        public static final String COLUMN_TOKEN = "accessToken";
    }
}