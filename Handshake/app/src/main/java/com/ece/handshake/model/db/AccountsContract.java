package com.ece.handshake.model.db;

import android.provider.BaseColumns;

public class AccountsContract {
    public AccountsContract() {}

    public static abstract class AccountsEntry implements BaseColumns {
        public static final String TABLE_NAME = "connected_accounts";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PLATFORM_NAME = "platformName";
        public static final String LINK_URI = "profileLinkUri";
        public static final String PIC_URI = "profilePicUri";
        public static final String TOKEN = "accessToken";
    }
}