package com.merit.myapplication.moduls;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merit.myapplication.R;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Comment;
import com.merit.myapplication.models.User;

import java.util.ArrayList;

/**
 * Created by merit on 7/14/2015.
 */
public class ListViewActivityCommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Comment> mComments;
    private ImageLoader mImageLoader;
    private OnClickItemCommentListener mOnClickItemCommentListener;

    public ListViewActivityCommentAdapter(Context mContext, ArrayList<Comment> mComments, OnClickItemCommentListener mOnClickItemCommentListener) {
        this.mContext = mContext;
        this.mComments = mComments;
        this.mOnClickItemCommentListener = mOnClickItemCommentListener;
        mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_activityrelationship, parent, false);

            holder = new ViewHolder();
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            ((RelativeLayout) convertView.findViewById(R.id.separator)).setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = mComments.get(position);
        if (comment.getUser().getProfilePicture() != null) {
            mImageLoader.DisplayImage(comment.getUser().getProfilePicture(), holder.ivAvatar);
            holder.ivAvatar.setOnClickListener(new OnClickItemComment(holder, comment));
        }
        if (comment.getText() != null) {
            String commentContent = "(user)" + comment.getUser().getUserName() + "(/user)" + " " + comment.getText() + "<br>" + "<font color='gray'>" + comment.getCreatedTime() + "</font>";
            LinkedTextView.autoLink(holder.tvContent, commentContent, new OnClickItemComment(holder, comment));
        }

        return convertView;
    }

    private class ViewHolder {
        CircleImageView ivAvatar;
        //ImageView btnFollow;
        TextView tvContent;
        //RelativeLayout itemLvRelationship;
    }

    private class OnClickItemComment implements LinkedTextView.OnClickListener, View.OnClickListener {

        ViewHolder holder;
        Comment mComment;

        public OnClickItemComment(ViewHolder holder, Comment mComment) {
            this.holder = holder;
            this.mComment = mComment;
        }

        @Override
        public void onLinkClicked(String link) {
            mOnClickItemCommentListener.onLinkClicked(link, mComment.getUser().getId());
        }

        @Override
        public void onClicked() {
            mOnClickItemCommentListener.onTextClicked();
        }

        @Override
        public void onClick(View v) {
            if (v == holder.ivAvatar) {
                mOnClickItemCommentListener.onClickAvatar(v, mComment.getUser().getId());
            }
        }
    }

    public interface OnClickItemCommentListener {

        public void onLinkClicked(String link, String cmtUserId);

        public void onClickAvatar(View v, String cmtUserId);

        public void onTextClicked();
    }

}
