package com.ece.handshake.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ece.handshake.model.db.AccountsContract.AccountsEntry;
import com.ece.handshake.model.db.PendingConnectionsContract.PendingConnectionEntry;

public class DBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Accounts.db";

    private static final String SQL_CREATE_ENTRY =
            "CREATE TABLE IF NOT EXISTS " + AccountsEntry.TABLE_NAME + " (" +
                    AccountsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AccountsEntry.FIRST_NAME + " TEXT, " +
                    AccountsEntry.LAST_NAME + " TEXT, " +
                    AccountsEntry.PLATFORM_NAME + " TEXT, " +
                    AccountsEntry.LINK_URI + " TEXT, " +
                    AccountsEntry.PIC_URI + " TEXT, " +
                    AccountsEntry.TOKEN + " TEXT" +
            ")";

    private static final String SQL_CREATE_ENTRY2  =
            "CREATE TABLE IF NOT EXISTS " + PendingConnectionEntry.TABLE_NAME + " (" +
                    PendingConnectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PendingConnectionEntry.FIRST_NAME + " TEXT, " +
                    PendingConnectionEntry.LAST_NAME + " TEXT, " +
                    PendingConnectionEntry.PLATFORM_NAME + " TEXT, " +
                    PendingConnectionEntry.LINK_URI + " TEXT, " +
                    PendingConnectionEntry.PIC_URI + " TEXT, " +
                    PendingConnectionEntry.TOKEN + " TEXT, " +
                    PendingConnectionEntry.TIME_ENTERED + " INTEGER, " +
                    PendingConnectionEntry.IS_PHONE_CONTACT + " INTEGER" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccountsEntry.TABLE_NAME + ", " + PendingConnectionEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRY);
        db.execSQL(SQL_CREATE_ENTRY2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
