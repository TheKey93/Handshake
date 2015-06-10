package com.ece.handshake;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesManager {
    private final static String BASIC_USER_INFO = "BasicUserInfoFile";

    public final static String GPLUS_FIRST_NAME = "GPlusFirstName";
    public final static String GPLUS_LAST_NAME = "GPlusLastName";
    public final static String GPLUS_PHOTO = "GPlusPhoto";
    public final static String GPLUS_EMAIL = "GPlusEmail";
    public final static String GPLUS_URL = "GPlusURL";

    public static void saveBasicInfo(Context context, Map<String, String> values) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(BASIC_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(GPLUS_FIRST_NAME, values.get(GPLUS_FIRST_NAME));
        editor.putString(GPLUS_LAST_NAME, values.get(GPLUS_LAST_NAME));
        editor.putString(GPLUS_EMAIL, values.get(GPLUS_EMAIL));
        editor.putString(GPLUS_URL, values.get(GPLUS_URL));
        editor.commit();
    }

}
