package com.ece.handshake.views;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<SMAccount> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mPlatformImage;
        private CircleImageView mProfileImage;
        private TextView mPlatformName;
        private TextView mAccountUserId;

        public ViewHolder(View v) {
            super(v);
            mPlatformImage = (CircleImageView) v.findViewById(R.id.platform_image);
            mProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
            mPlatformName = (TextView) v.findViewById(R.id.platform_name);
            mAccountUserId = (TextView) v.findViewById(R.id.account_user_id);
        }
    }

    public AccountsAdapter(final Context context, ArrayList<SMAccount> myDataset) {
        mDataset = myDataset;
        mContext = context;
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

        Picasso.with(mContext).load(account.getProfilePicUri()).into(viewHolder.mProfileImage);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
