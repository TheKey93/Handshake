package com.ece.handshake.views;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ece.handshake.R;
import com.ece.handshake.helper.GPSTracker;
import com.ece.handshake.helper.JSONParser;
import com.ece.handshake.model.db.MapsContract;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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


public class GMapsFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ProgressDialog pDialog;
    private static double latitude, longitude;
    private MapView mMapView;

    JSONParser jParser = new JSONParser();
    JSONArray locations = null;
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            //setUpMapIfNeeded();
            new LoadAllProducts().execute(latitude, longitude);
        } else {
            gps.showSettingsAlert();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.map_fragment, container, false);
        mMapView = (MapView) layout.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMap = mMapView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        MapsInitializer.initialize(this.getActivity());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10);
        mMap.animateCamera(cameraUpdate);
        /*mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location"));
        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(latitude,
                        longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);*/
        return layout;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
            pDialog = new ProgressDialog(getActivity());
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
