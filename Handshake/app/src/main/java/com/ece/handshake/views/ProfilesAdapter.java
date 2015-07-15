package com.ece.handshake.views;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder>{
    private ArrayList<SMAccount> mDataset;
    private ProfilesPresenter mPresenter;
    private final Context mContext;
    private final String mProfileName;

    public ProfilesAdapter(final Context context, ArrayList<SMAccount> myDataset, final String profileName, ProfilesPresenter presenter) {
        mDataset = myDataset;
        mPresenter = presenter;
        mContext = context;
        mProfileName = profileName;
    }

    public SMAccount getAccount(final int position) {
        return mDataset.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SMAccount account = mDataset.get(position);
        holder.mName.setText(account.getName());
        holder.mPlatformName.setText(account.getPlatformName());
        holder.mPlatformImage.setImageDrawable(MediaPlatformHelper.getAccountImageResource(account.getPlatformName()));

        //TODO: Use Placeholder if no image or error
        Picasso.with(mContext).load(account.getProfilePicUri()).into(holder.mProfileImage);

        final String platform = account.getPlatformName();
        boolean test = mPresenter.isPlatformEnabledForProfile(platform, mProfileName);
        holder.mSwitch.setChecked(mPresenter.isPlatformEnabledForProfile(platform, mProfileName));
        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.togglePlatform(account.getPlatformName(), position, mProfileName, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPlatformImage;
        private TextView mPlatformName;
        private TextView mName;
        private SwitchCompat mSwitch;
        private CircleImageView mProfileImage;


        public ViewHolder(View v) {
            super(v);
            mPlatformImage = (ImageView) v.findViewById(R.id.platform_image);
            mPlatformName = (TextView) v.findViewById(R.id.platform_name);
            mName = (TextView) v.findViewById(R.id.name);
            mSwitch = (SwitchCompat) v.findViewById(R.id.platform_enabled_switch);
            mProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
        }
    }
}
