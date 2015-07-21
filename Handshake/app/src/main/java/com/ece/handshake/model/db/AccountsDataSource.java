package com.ece.handshake.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.ece.handshake.model.data.Connection;
import com.ece.handshake.model.data.PhoneContact;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.model.db.AccountsContract.AccountsEntry;
import com.ece.handshake.model.db.PendingConnectionsContract.PendingConnectionEntry;

import java.util.ArrayList;

public class AccountsDataSource {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public AccountsDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertConnectedAccount(SMAccount account) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AccountsEntry.FIRST_NAME, account.getFirstName());
        values.put(AccountsEntry.LAST_NAME, account.getLastName());
        values.put(AccountsEntry.PLATFORM_NAME, account.getPlatformName());
        values.put(AccountsEntry.LINK_URI, account.getLinkUri().toString());
        values.put(AccountsEntry.PIC_URI, account.getProfilePicUri().toString());
        values.put(AccountsEntry.TOKEN, account.getAccessToken());

        long newRowId = db.insertOrThrow(AccountsEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public void insertPendingConnection(ArrayList<Connection> connections) {
        db = dbHelper.getWritableDatabase();
        for(Connection connection : connections) {
            long currentTime = System.currentTimeMillis();
            ContentValues values = new ContentValues();

            if (connection instanceof SMAccount) {
                SMAccount account = (SMAccount) connection;
                values.put(PendingConnectionEntry.FIRST_NAME, account.getFirstName());
                values.put(PendingConnectionEntry.LAST_NAME, account.getLastName());
                values.put(PendingConnectionEntry.PLATFORM_NAME, account.getPlatformName());
                values.put(PendingConnectionEntry.LINK_URI, account.getLinkUri().toString());
                values.put(PendingConnectionEntry.PIC_URI, account.getProfilePicUri().toString());
                values.put(PendingConnectionEntry.TOKEN, account.getAccessToken());
                values.put(PendingConnectionEntry.TIME_ENTERED, currentTime);
                values.put(PendingConnectionEntry.IS_PHONE_CONTACT, 0);
            } else if (connection instanceof PhoneContact){
                PhoneContact contact = (PhoneContact) connection;
                values.put(PendingConnectionEntry.FIRST_NAME, contact.getFirstName());
                values.put(PendingConnectionEntry.LAST_NAME, contact.getLastName());
                if (contact.getProfilePicUri() != null) {
                    values.put(PendingConnectionEntry.PIC_URI, contact.getProfilePicUri().toString());

                }
                values.put(PendingConnectionEntry.TIME_ENTERED, currentTime);
                values.put(PendingConnectionEntry.IS_PHONE_CONTACT, 1);
            }
            db.insertOrThrow(PendingConnectionEntry.TABLE_NAME, null, values);
        }
        db.close();
    }

    public ArrayList<SMAccount> getConnectedAccounts() {
        db = dbHelper.getReadableDatabase();

        String[] projection = {AccountsEntry.FIRST_NAME, AccountsEntry.LAST_NAME, AccountsEntry.LINK_URI, AccountsEntry.PLATFORM_NAME,
                AccountsEntry.PIC_URI, AccountsEntry.TOKEN};

        String sortOrder = AccountsEntry.PLATFORM_NAME + " DESC";

        Cursor c = db.query(
                AccountsEntry.TABLE_NAME,  // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );
        ArrayList<SMAccount> list = new ArrayList<>();

        while(c.moveToNext()) {
            SMAccount account = new SMAccount();
            account.setFirstName(c.getString(c.getColumnIndex(AccountsEntry.FIRST_NAME)));
            account.setLastName(c.getString(c.getColumnIndex(AccountsEntry.LAST_NAME)));
            account.setPlatformName(c.getString(c.getColumnIndex(AccountsEntry.PLATFORM_NAME)));
            account.setLinkUri(Uri.parse(c.getString(c.getColumnIndex(AccountsEntry.LINK_URI))));
            account.setProfilePicUri(Uri.parse(c.getString(c.getColumnIndex(AccountsEntry.PIC_URI))));
            account.setAccessToken(c.getString(c.getColumnIndex(AccountsEntry.TOKEN)));

            list.add(account);
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<Connection> getPendingConnections() {
        db = dbHelper.getReadableDatabase();

        String[] projection = {PendingConnectionEntry.FIRST_NAME, PendingConnectionEntry.LAST_NAME, PendingConnectionEntry.PLATFORM_NAME, PendingConnectionEntry.LINK_URI,
                PendingConnectionEntry.PIC_URI, PendingConnectionEntry.TOKEN, PendingConnectionEntry.TIME_ENTERED, PendingConnectionEntry.IS_PHONE_CONTACT};

        String sortOrder = PendingConnectionEntry.TIME_ENTERED + " DESC";

        Cursor c = db.query(
                PendingConnectionEntry.TABLE_NAME,  // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );
        ArrayList<Connection> list = new ArrayList<>();

        while(c.moveToNext()) {
            final boolean isPhoneContact = c.getInt(c.getColumnIndex(PendingConnectionEntry.IS_PHONE_CONTACT)) != 0;

            if (!isPhoneContact) {
                SMAccount account = new SMAccount();
                account.setFirstName(c.getString(c.getColumnIndex(PendingConnectionEntry.FIRST_NAME)));
                account.setLastName(c.getString(c.getColumnIndex(PendingConnectionEntry.LAST_NAME)));
                account.setPlatformName(c.getString(c.getColumnIndex(PendingConnectionEntry.PLATFORM_NAME)));
                //if (c.getString(c.getColumnIndex(PendingConnectionEntry.LINK_URI)) != null && !c.getString(c.getColumnIndex(PendingConnectionEntry.LINK_URI)).isEmpty())
                String link = c.getString(c.getColumnIndex(PendingConnectionEntry.LINK_URI));
                String prof = c.getString(c.getColumnIndex(PendingConnectionEntry.PIC_URI));

                if (!link.contains("http://") && !link.contains("https://"))
                    link = "http://" + link;
                account.setLinkUri(Uri.parse(link));
                account.setProfilePicUri(Uri.parse(prof));
                account.setAccessToken(c.getString(c.getColumnIndex(PendingConnectionEntry.TOKEN)));

                account.setTime(c.getLong(c.getColumnIndex(PendingConnectionEntry.TIME_ENTERED)));
                list.add(account);
            } else {
                PhoneContact contact = new PhoneContact();
                contact.setFirstName(c.getString(c.getColumnIndex(PendingConnectionEntry.FIRST_NAME)));
                contact.setLastName(c.getString(c.getColumnIndex(PendingConnectionEntry.LAST_NAME)));
                final String profilePicUri = c.getString(c.getColumnIndex(PendingConnectionEntry.PIC_URI));
                if (profilePicUri != null)
                    contact.setProfilePicUri(Uri.parse(profilePicUri));
                contact.setTime(c.getLong(c.getColumnIndex(PendingConnectionEntry.TIME_ENTERED)));
                list.add(contact);
            }
        }

        c.close();
        db.close();
        return list;
    }
}
