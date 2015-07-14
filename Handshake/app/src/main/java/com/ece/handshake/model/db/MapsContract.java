package com.ece.handshake.model.db;


import android.provider.BaseColumns;

/**
 * Created by Dr. Kamlesh on 08-07-2015.
 */
public class MapsContract {

    public MapsContract(){}

    public static abstract class MapsEntry implements BaseColumns {
        public static final String url_create_user = "http://handshakeonline.net16.net/webservices/Insert.php";
        public static final String url_delete_user = "http://handshakeonline.net16.net/webservices/Delete.php";
        public static String url_all_products = "http://handshakeonline.net16.net/webservices/index.php";
        public static final String TAG_SUCCESS = "success";
        public static final String TAG_LOCATION = "LocationInfo";
        public static final String TAG_UID = "UserId";
        public static final String TAG_DISTANCE = "distance";
        public static final String TAG_LATITUDE = "Latitude";
        public static final String TAG_LONGITUDE = "Longitude";
    }
}
