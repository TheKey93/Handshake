package com.ece.handshake.model.db;

import android.os.AsyncTask;

import com.ece.handshake.helper.JSONParser;
import com.ece.handshake.model.db.MapsContract.MapsEntry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Dr. Kamlesh on 08-07-2015.
 */
public class MapsDataSource {
    JSONParser jsonParser = new JSONParser();

    public void InsertUser(String UserId, String Longitude, String Latitude){
        new CreateNewProduct().execute(UserId,Longitude,Latitude);
    }

    public void DeleteUser(String UserId){
        new DeleteNewProduct().execute(UserId);
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", args[0]));
            params.add(new BasicNameValuePair("longitude", args[1]));
            params.add(new BasicNameValuePair("latitude", args[2]));
            JSONObject json1 = jsonParser.makeHttpRequest(MapsEntry.url_create_user,"POST", params);
            return null;
        }
    }

    class DeleteNewProduct extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", args[0]));
            JSONObject json = jsonParser.makeHttpRequest(MapsEntry.url_delete_user, "POST", params);
            return null;
        }
    }
}
