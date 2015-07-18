package com.ece.handshake.views;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.ece.handshake.R;
import com.ece.handshake.helper.GPSTracker;
import com.ece.handshake.helper.JSONParser;
import com.ece.handshake.model.db.MapsContract;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ProgressDialog pDialog;
    private static double latitude, longitude;

    JSONParser jParser = new JSONParser();
    JSONArray locations = null;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            setUpMapIfNeeded();
            Toast.makeText(getApplicationContext(), "After setup map if needed", Toast.LENGTH_LONG).show();
            new LoadAllProducts().execute(latitude, longitude);
        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Toast.makeText(getApplicationContext(), "In setup map if needed - mMap not null", Toast.LENGTH_LONG).show();
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location"));
        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(latitude,
                        longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    class LoadAllProducts extends AsyncTask<Double, Void, String> {
        ArrayList<HashMap<String, String>> locationList = new ArrayList<HashMap<String, String>>();
        String error = "";

        protected void onPreExecute() {
            super.onPreExecute();
            /*if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();*/
            //
            pDialog = new ProgressDialog(MapsFragment.this);
            pDialog.setMessage("Loading locations. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            /*} else {
                gps.showSettingsAlert();
            }*/
        }

        protected String doInBackground(Double... args) {
            //Toast.makeText(getApplicationContext(), "In doInBackground", Toast.LENGTH_LONG).show();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            /*params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
            params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));*/
            params.add(new BasicNameValuePair("latitude", Double.toString(args[0])));
            params.add(new BasicNameValuePair("longitude", Double.toString(args[1])));
            JSONObject json = jParser.makeHttpRequest(MapsContract.MapsEntry.url_all_products, "GET", params);
            Log.d("All Locations: ", json.toString());
            try {
                locations = json.getJSONArray(MapsContract.MapsEntry.TAG_LOCATION);
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject c = locations.getJSONObject(i);
                    String uid = c.getString(MapsContract.MapsEntry.TAG_UID);
                    String distance = c.getString(MapsContract.MapsEntry.TAG_DISTANCE);
                    String latitude = c.getString(MapsContract.MapsEntry.TAG_LATITUDE);
                    String longitude = c.getString(MapsContract.MapsEntry.TAG_LONGITUDE);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(MapsContract.MapsEntry.TAG_UID, uid);
                    map.put(MapsContract.MapsEntry.TAG_DISTANCE, distance);
                    map.put(MapsContract.MapsEntry.TAG_LATITUDE, latitude);
                    map.put(MapsContract.MapsEntry.TAG_LONGITUDE, longitude);
                    locationList.add(map);
                }
                error += json.toString() + "From DoInBackground";
                Thread.sleep(5000);
            } catch (JSONException e) {
                error += e.getLocalizedMessage() + "JSONExecption";

            } catch (InterruptedException e) {
                error += e.getMessage() + "InterruptedExeption";
            }
            //Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
            return error;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            Object LATITUDE = "Latitude";
            Object LONGITUDE = "Longitude";
            Object DISTANCE = "distance";
            ArrayList<LatLng> latLngArray = new ArrayList<LatLng>();
            //Toast.makeText(getApplicationContext(),"Location List size in post execute:"+locationList.size(), Toast.LENGTH_LONG).show();
            for (HashMap<String, String> locations : locationList) {
                LatLng latLon = new LatLng(Double.parseDouble(locations.get(LONGITUDE)),Double.parseDouble(locations.get(LATITUDE)));
                mMap.addMarker(new MarkerOptions().position(latLon).title("DISTANCE:"+(Double.parseDouble(locations.get(DISTANCE)) *1000) + "m"));
            }

        }
    }
}