package com.merit.myapplication.moduls;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.merit.myapplication.R;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.User;
import com.merit.myapplication.models.YouListViewItem;

import java.util.ArrayList;

/**
 * Created by merit on 7/14/2015.
 */
public class ListViewActivityRelationshipAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> mUsers;
    private ImageLoader mImageLoader;

    public ListViewActivityRelationshipAdapter(Context mContext, ArrayList<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_activityrelationship, parent, false);

            holder = new ViewHolder();
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.btnFollow = (ImageView) convertView.findViewById(R.id.btnFollow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User mUser = mUsers.get(position);
        if (mUser.getProfilePicture() != null)
            mImageLoader.DisplayImage(mUser.getProfilePicture(), holder.ivAvatar);
        if (mUser.getUserName() != null && mUser.getFullName() != null) {
            String content = "<b>" + mUser.getUserName() + "</b><br><font color='gray'>" + mUser.getFullName() + "</font>";
            holder.tvContent.setText(Html.fromHtml(content));
        }


        return convertView;
    }

    private class ViewHolder {
        CircleImageView ivAvatar;
        ImageView btnFollow;
        TextView tvContent;
    }
}
