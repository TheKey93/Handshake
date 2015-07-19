package com.ece.handshake.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.handshake.R;
import com.ece.handshake.presenters.BumpPresenter;


public class BumpFragment extends Fragment implements IBumpView{
    private Spinner mActiveProfileSpinner;
    private TextView mNfcStatus;
    private TextView mEnableNfcButton;

    private BumpPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new BumpPresenter(getActivity(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.bump_fragment, container, false);

        mEnableNfcButton = (Button) layout.findViewById(R.id.enable_nfc_button);
        mActiveProfileSpinner = (Spinner) layout.findViewById(R.id.active_profile_spinner);
        mNfcStatus = (TextView) layout.findViewById(R.id.nfc_status);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.profiles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActiveProfileSpinner.setAdapter(adapter);

        final int profilePos = adapter.getPosition(mPresenter.getActiveProfile());
        mActiveProfileSpinner.setSelection(profilePos);

        mActiveProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setActiveProfile(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEnableNfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        mPresenter.isNfcEnabled();
        return layout;
    }

    @Override
    public void setNfcStatus(boolean isEnabled) {
        mNfcStatus.setText(isEnabled ? getString(R.string.bump_nfc_enabled) : getString(R.string.bump_nfc_enabled));
        mNfcStatus.setTextColor(isEnabled ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        mEnableNfcButton.setVisibility(isEnabled ? View.INVISIBLE : View.VISIBLE);
    }
}