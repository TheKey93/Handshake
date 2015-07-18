package com.ece.handshake.views;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ece.handshake.R;
import com.ece.handshake.helper.GPSTracker;
import com.ece.handshake.model.db.MapsDataSource;

public class PreferencesFragment extends PreferenceFragment {
    GPSTracker gps;
    double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference);

        gps = new GPSTracker(this.getActivity());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        final CheckBoxPreference pref = (CheckBoxPreference) findPreference("pref_chk_shareLocation");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                MapsDataSource mapsDataSource = new MapsDataSource();
                final String deviceId = ((MainActivity) getActivity()).getDeviceId();
                if (pref.isChecked()) {
                    mapsDataSource.InsertUser(deviceId, Double.toString(longitude), Double.toString(latitude));
                } else {
                    mapsDataSource.DeleteUser(deviceId);
                }
                return true;
            }
        });
    }
}
