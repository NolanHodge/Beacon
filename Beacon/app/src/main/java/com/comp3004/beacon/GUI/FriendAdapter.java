package com.comp3004.beacon.GUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp3004.beacon.Networking.GetImage;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;

import java.util.ArrayList;

/**
 * Created by Jason on 19/11/2016.
 */

public class FriendAdapter extends ArrayAdapter<BeaconUser> {
    private static class ViewHolder {
        public TextView name;
        public ImageView icon;
        public View layout;
    }

    ArrayList<BeaconUser> friends;

    public FriendAdapter(Context context, ArrayList<BeaconUser> objects) {
        super(context, R.layout.friend_list_item, objects);
        friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friend_list_item, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.friend_name);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.friend_icon);
            viewHolder.layout = convertView.findViewById(R.id.fr_layout);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        new GetImage() {
            @Override
            protected void onPostExecute(Bitmap[] bitmaps) {
                super.onPostExecute(bitmaps);
                viewHolder.icon.setImageBitmap(bitmaps[0]);
            }
        }.execute(friends.get(position).getPhotoUrl());
        viewHolder.name.setText(friends.get(position).getDisplayName());


        viewHolder.name.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));

        if ((position % 2) == 1) {
            viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(),(R.color.colorPrimary)));
        } else {
            viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(),(R.color.colorPrimaryDark)));
        }

        return convertView;
    }
}
