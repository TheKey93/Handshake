package com.ece.handshake.views;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ece.handshake.R;
import com.ece.handshake.helper.MediaPlatformHelper;
import com.ece.handshake.model.data.Connection;
import com.ece.handshake.model.data.PhoneContact;
import com.ece.handshake.model.data.SMAccount;
import com.ece.handshake.presenters.PendingConnectionsPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PendingConnectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Connection> mDataset;
    private PendingConnectionsPresenter mPresenter;

    public PendingConnectionsAdapter(PendingConnectionsPresenter presenter, ArrayList<Connection> data) {
        mDataset =  data;
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v;

        switch(viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_connection_row, parent, false);
                holder = new SMAccountViewHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_phone_contact, parent, false);
                holder = new ContactViewHolder(v);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        Connection connection = mDataset.get(position);
        if (connection instanceof SMAccount)
            type = 0;
        else
            type = 1;
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Connection connection = mDataset.get(position);

        if (getItemViewType(position) == 0) {
            SMAccountViewHolder holder = (SMAccountViewHolder) viewHolder;
            final SMAccount account = (SMAccount) connection;
            holder.mPlatformName.setText(account.getPlatformName());
            holder.mPlatformImage.setImageDrawable(MediaPlatformHelper.getAccountImageResource(account.getPlatformName()));
            holder.mAccountUserId.setText(account.getName());
            holder.mPlatformName.setText(account.getPlatformName());
            holder.mPlatformImage.setImageDrawable(MediaPlatformHelper.getAccountImageResource(account.getPlatformName()));
            Picasso.with(mPresenter.getContext()).load(account.getProfilePicUri()).into(holder.mProfileImage);
            holder.mRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(Intent.ACTION_VIEW, account.getLinkUri());
                    mPresenter.getContext().startActivity(i);
                }
            });
        } else {
            ContactViewHolder holder = (ContactViewHolder) viewHolder;
            final PhoneContact contact = (PhoneContact) connection;

            holder.mContactName.setText(contact.getName());
            //holder.mContactPhoneNumber.setText(contact.getPhoneNumber());
            holder.mContactPhoneNumber.setText(contact.getPhoneNumber());
            //Picasso.with(mPresenter.getContext()).load(contact.getProfilePicUri()).into(holder.mContactPhoto);
            holder.mRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    newContactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    newContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
                    newContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhoneNumber());
                    mPresenter.getContext().startActivity(newContactIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class SMAccountViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mPlatformImage;
        private CircleImageView mProfileImage;
        private TextView mPlatformName;
        private TextView mAccountUserId;
        private RelativeLayout mRow;

        public SMAccountViewHolder(View v) {
            super(v);
            mPlatformImage = (CircleImageView) v.findViewById(R.id.platform_image);
            mProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
            mPlatformName = (TextView) v.findViewById(R.id.platform_name);
            mAccountUserId = (TextView) v.findViewById(R.id.name);
            mRow = (RelativeLayout) v.findViewById(R.id.pending_connection_row);

        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mContactPhoto;
        private TextView mContactName;
        private TextView mContactPhoneNumber;
        private RelativeLayout mRow;

        public ContactViewHolder(View v) {
            super(v);
            mContactPhoto = (CircleImageView) v.findViewById(R.id.profile_image);
            mContactName = (TextView) v.findViewById(R.id.name);
            mContactPhoneNumber = (TextView) v.findViewById(R.id.phone_number);
            mRow = (RelativeLayout) v.findViewById(R.id.pending_connection_row);
        }
    }
}
