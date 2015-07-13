package com.merit.myapplication.moduls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.merit.myapplication.R;

import java.util.ArrayList;

import com.merit.myapplication.models.YouListViewItem;

/**
 * Created by merit on 6/29/2015.
 */
public class ListViewFragmentYouAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<YouListViewItem> youListViewItems;

    public ListViewFragmentYouAdapter(Context mContext, ArrayList<YouListViewItem> youListViewItems) {
        this.mContext = mContext;
        this.youListViewItems = youListViewItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return youListViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return youListViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        private CircleImageView ivAvatar;
        private ImageView ivButton;
        private TextView tvContent;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_fragmentyou, parent, false);
            holder = new ViewHolder();

            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            holder.ivButton = (ImageView) convertView.findViewById(R.id.ivButton);
            holder.tvContent = (TextView) convertView.findViewById(R.id.ivContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set content for item
        YouListViewItem youListViewItem = youListViewItems.get(position);
        holder.ivAvatar.setImageDrawable(youListViewItem.getAvatar());
        holder.ivButton.setImageDrawable(youListViewItem.getButton());
        holder.tvContent.setText(youListViewItem.getContent());

        // set event for item
        holder.ivAvatar.setOnClickListener(new ItemsEvent(holder, convertView, position));
        holder.ivButton.setOnClickListener(new ItemsEvent(holder, convertView, position));

        return convertView;
    }

    private class ItemsEvent implements View.OnClickListener {
        ViewHolder holder;
        View view;
        int position;

        public ItemsEvent(ViewHolder holder, View view, int position) {
            this.holder = holder;
            this.view = view;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (v == holder.ivAvatar) {
                ivAvatarEvent();
            } else if (v == holder.ivButton) {
                ivButtonEvent();
            }
        }

        // CODE ivAvatar EVENT
        private void ivAvatarEvent() {
            // TO DO: WHEN CLICKING ivAvatar

            Toast.makeText(view.getContext(), "ivAvatar " + position + " is clicked", Toast.LENGTH_SHORT).show();

        }

        // CODE ivButton EVENT
        private void ivButtonEvent() {
            // TO DO: WHEN CLICKING ivButton

            Toast.makeText(view.getContext(), "ivButton " + position + " is clicked", Toast.LENGTH_SHORT).show();
        }
    }

}
