package com.ece.handshake.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.handshake.R;
import com.ece.handshake.helper.MediaPlatformHelper;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.presenters.ProfilesPresenter;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder> implements IProfilesView{
    private ArrayList<SMAccount> mDataset;
    private ProfilesPresenter mPresenter;

    public ProfilesAdapter(Context context, ArrayList<SMAccount> myDataset) {
        mDataset = myDataset;
        mPresenter = new ProfilesPresenter(context, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SMAccount account = mDataset.get(position);
        holder.mAccountUserId.setText(account.getName());
        holder.mPlatformName.setText(account.getPlatformName());
        holder.mPlatformImage.setImageDrawable(MediaPlatformHelper.getAccountImageResource(account.getPlatformName()));

        Profile profile = Profile.getCurrentProfile();
        if (profile != null)
            holder.mProfileImage.setProfileId(profile.getId());

        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mPresenter.enablePlatform(account.getPlatformName(), position);
                else
                    mPresenter.disablePlatform(account.getPlatformName(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void highlightPlatformRowGreen(int rowPosition) {

    }

    @Override
    public void highlightPlatformRowRed(int rowPosition) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPlatformImage;
        private TextView mPlatformName;
        private TextView mAccountUserId;
        private SwitchCompat mSwitch;
        private CardView  mCardView;
        private ProfilePictureView mProfileImage;


        public ViewHolder(View v) {
            super(v);
            mPlatformImage = (ImageView) v.findViewById(R.id.platform_image);
            mPlatformName = (TextView) v.findViewById(R.id.platform_name);
            mAccountUserId = (TextView) v.findViewById(R.id.account_user_id);
            mSwitch = (SwitchCompat) v.findViewById(R.id.platform_enabled_switch);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mProfileImage = (ProfilePictureView) v.findViewById(R.id.profile_image);
        }
    }
}
