package com.ece.handshake.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.handshake.R;
import com.ece.handshake.helper.MediaPlatformHelper;
import com.ece.handshake.model.data.SMAccount;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private ArrayList<SMAccount> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPlatformImage;
        private ProfilePictureView mProfileImage;
        private TextView mPlatformName;
        private TextView mAccountUserId;
        private CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            mPlatformImage = (ImageView) v.findViewById(R.id.platform_image);
            mProfileImage = (ProfilePictureView) v.findViewById(R.id.profile_image);
            mPlatformName = (TextView) v.findViewById(R.id.platform_name);
            mAccountUserId = (TextView) v.findViewById(R.id.account_user_id);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public AccountsAdapter(ArrayList<SMAccount> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SMAccount account = mDataset.get(i);
        viewHolder.mAccountUserId.setText(account.getName());
        viewHolder.mPlatformName.setText(account.getPlatformName());
        viewHolder.mPlatformImage.setImageDrawable(MediaPlatformHelper.getAccountImageResource(account.getPlatformName()));

        Profile profile = Profile.getCurrentProfile();
        if (profile != null)
            viewHolder.mProfileImage.setProfileId(profile.getId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
