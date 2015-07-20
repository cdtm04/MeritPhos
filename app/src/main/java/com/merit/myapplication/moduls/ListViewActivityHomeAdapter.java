package com.merit.myapplication.moduls;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.activities.ActivityAccount;
import com.merit.myapplication.activities.ActivityComment;
import com.merit.myapplication.activities.ActivityHomeGroup;
import com.merit.myapplication.activities.ActivityRelationship;
import com.merit.myapplication.activities.MainActivity;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Post;

import java.io.Serializable;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by merit on 7/2/2015.
 */
public class ListViewActivityHomeAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer, View.OnTouchListener {
    private final Context mContext;
    private ArrayList<Post> mPostedMediaItems;

    private ImageLoader mImageLoader;

    public ListViewActivityHomeAdapter(Context context, ArrayList<Post> postedMediaItems) {
        mContext = context;
        mPostedMediaItems = postedMediaItems;
        mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        ViewHeaderHolder holder = null;
        if (view == null) {
            holder = new ViewHeaderHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.itemheader_listview_activityhome, viewGroup, false);
            holder.ivAvatar = (CircleImageView) view.findViewById(R.id.ivAvatar);
            holder.tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            holder.tvPostedTime = (TextView) view.findViewById(R.id.tvPostedTime);
            holder.btnAccount = (RelativeLayout) view.findViewById(R.id.btnAccount);
            view.setTag(holder);
        } else {
            holder = (ViewHeaderHolder) view.getTag();
        }

        // set value on value
        Post mPostedMediaItem = mPostedMediaItems.get(i);
        mImageLoader.DisplayImage(mPostedMediaItem.getUserOfPost().getProfilePicture(), holder.ivAvatar);
        holder.tvUsername.setText(mPostedMediaItem.getUserOfPost().getUserName());
        holder.tvPostedTime.setText(mPostedMediaItem.getCreatedTime());

        // set event for items
        holder.btnAccount.setOnClickListener(new EventItems(mPostedMediaItem, holder, null, view, i));

        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mPostedMediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostedMediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_activityhome, parent, false);
            holder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            holder.btnLike = (ImageView) convertView.findViewById(R.id.btnLike);
            holder.btnComment = (ImageView) convertView.findViewById(R.id.btnComment);
            holder.btnOption = (ImageView) convertView.findViewById(R.id.btnOption);
            holder.btnLikeCount = (RelativeLayout) convertView.findViewById(R.id.btnLikeCount);
            holder.tvCountLikes = (TextView) convertView.findViewById(R.id.tvCountLikes);
            holder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);
            holder.btnVideo = (ImageView) convertView.findViewById(R.id.btnVideo);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set value on items
        Post mPostedMediaItem = mPostedMediaItems.get(position);
        mImageLoader.DisplayImage(mPostedMediaItem.getImage().getUrl(), holder.ivPicture);
        holder.btnOption.setImageAlpha(135);
        holder.btnComment.setImageAlpha(135);
        if (mPostedMediaItem.isLiked())
            holder.btnLike.setImageResource(R.drawable.icon_liked);
        else
            holder.btnLike.setImageResource(R.drawable.icon_like);
        holder.tvCountLikes.setText((mPostedMediaItem.getCountLikes() == 1) ? mPostedMediaItem.getCountLikes() + " like" : mPostedMediaItem.getCountLikes() + " likes");

        // if the post is video, set visible and event
        if (mPostedMediaItem.getType().equals("video") || mPostedMediaItem.getVideo() != null) {
            holder.btnVideo.setVisibility(View.VISIBLE);
            holder.btnVideo.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        } else {
            holder.btnVideo.setVisibility(View.GONE);
        }

        // set value textview comments:
        String contentOfTvComments = "<font color='gray'>Add a comment</font>";
        String captionOfUser = "";
        if (mPostedMediaItem.getCaptionOfPost() != null)
            captionOfUser = "(user)" + mPostedMediaItem.getUserOfPost().getUserName() + "(/user) " + mPostedMediaItem.getCaptionOfPost().getTextOfCaption() + "<br>";
        if (mPostedMediaItem.getComments().size() > 0) {
            String countComments = "<font color='gray'>View all " + ((mPostedMediaItem.getCountComments() + ((mPostedMediaItem.getCountComments() == 1) ? " comment</font>" : " comments</font>")));
            String comments = "<br>";
            for (int i = 0; i < mPostedMediaItem.getComments().size(); i++) {
                String account = mPostedMediaItem.getComments().get(i).getUser().getUserName();
                String comment = mPostedMediaItem.getComments().get(i).getText();
                // get 3 comments
                if (i <= 2)
                    comments += "(user)" + account + "(/user) " + comment + "<br>";
            }
            contentOfTvComments = countComments + comments + "<font color='gray'>Add a comment</font>";
        }
        contentOfTvComments = captionOfUser + contentOfTvComments;
        // set text & event for tvComments
        LinkedTextView.autoLink(holder.tvComments, contentOfTvComments, new EventItems(mPostedMediaItem, null, holder, convertView, position));

        // set event for items
        holder.btnLike.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnComment.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.tvCountLikes.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));

        holder.btnComment.setOnTouchListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnTouchListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));

        return convertView;
    }


    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position + 1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private class ViewHolder {
        ImageView ivPicture;
        ImageView btnLike, btnComment, btnOption, btnVideo;
        TextView tvCountLikes, tvComments;
        RelativeLayout btnLikeCount;
    }

    private class ViewHeaderHolder {
        RelativeLayout btnAccount;
        CircleImageView ivAvatar;
        TextView tvUsername, tvPostedTime;
    }


    private class EventItems implements View.OnClickListener, LinkedTextView.OnClickListener, View.OnTouchListener {
        Post mPostedMediaItem;
        ViewHeaderHolder viewHeaderHolder;
        ViewHolder viewHolder;
        View view;
        int position;

        public EventItems(Post mPostedMediaItem, ViewHeaderHolder viewHeaderHolder, ViewHolder viewHolder, View view, int position) {
            this.mPostedMediaItem = mPostedMediaItem;
            this.viewHeaderHolder = viewHeaderHolder;
            this.viewHolder = viewHolder;
            this.view = view;
            this.position = position;
        }


        // CODE tvComments EVENT
        private void tvCommentsEvent(String link) {
            if (link != null) {
                // TO DO WHEN CLICK tvComments on LINK HERE
                String userId = mPostedMediaItem.getUserOfPost().getId();
                for (int i = 0; i < mPostedMediaItem.getComments().size(); i++) {
                    if (mPostedMediaItem.getComments().get(i).getUser().getUserName().equals(link)) {
                        userId = mPostedMediaItem.getComments().get(i).getUser().getId();
                        break;
                    }
                }
                Intent iOpenUserInfoActivity = new Intent(mContext, ActivityAccount.class);
                iOpenUserInfoActivity.putExtra("ID", userId);
                iOpenUserInfoActivity.putExtra("PARENT", "HomeActivity");
                iOpenUserInfoActivity.putExtra("POSITION", position);
                View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityAccount", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                ActivityHomeGroup.groupHomeGroup.replaceView(view);
                //Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on link", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(mContext, ActivityComment.class);
                intent.putExtra("MPOST", mPostedMediaItem);
                intent.putExtra(MainActivity.PARENT, MainActivity.PARENT_HOME);
                mContext.startActivity(intent);

                //Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on text", Toast.LENGTH_SHORT).show();
            }
        }

        // CODE tvCountLikes EVENT

        private void tvCountLikesEvent() {
            // TO DO WHEN CLICK tvCountLikes HERE
            Intent iOpenRelationship = new Intent(mContext, ActivityRelationship.class);
            iOpenRelationship.putExtra("LABELNAME", InstagramApp.GET_LIKED_USERS);
            iOpenRelationship.putExtra("ID", mPostedMediaItem.getId());
            iOpenRelationship.putExtra(MainActivity.PARENT, MainActivity.PARENT_HOME);
            View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityRelationship", iOpenRelationship.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityHomeGroup.groupHomeGroup.replaceView(view);
            // Toast.makeText(mContext, "tvCountLikes " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnOption EVENT
        private void btnOptionEvent() {
            //viewHolder.btnOption.setImageAlpha(225);
            // TO DO WHEN CLICK btnOption HERE

            Toast.makeText(mContext, "btnOption " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnComment EVENT
        private void btnCommentEvent() {
            // TO DO WHEN CLICK btnComment HERE
            Intent intent = new Intent(mContext, ActivityComment.class);
            intent.putExtra("MPOST", mPostedMediaItem);
            intent.putExtra(MainActivity.PARENT, MainActivity.PARENT_HOME);
            mContext.startActivity(intent);
            //Toast.makeText(mContext, "btnComment " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnAccount EVENT
        private void btnLikeEvent() {
            // TO DO WHEN CLICK btnLike HERE
            // reset icon btnLike and update likeCount


            Toast.makeText(mContext, "btnLike " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnAccount EVENT
        private void btnAccountEvent() {
            // TO DO WHEN CLICK btnAccount HERE
            Intent iOpenUserInfoActivity = new Intent(mContext, ActivityAccount.class);
            iOpenUserInfoActivity.putExtra("ID", mPostedMediaItem.getUserOfPost().getId());
            iOpenUserInfoActivity.putExtra("PARENT", "HomeActivity");
            iOpenUserInfoActivity.putExtra("POSITION", position);
            View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityAccount", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityHomeGroup.groupHomeGroup.replaceView(view);
            //Toast.makeText(mContext, "btnAccount " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if (viewHeaderHolder != null) {
                if (v == viewHeaderHolder.btnAccount) btnAccountEvent();
            } else {
                if (v == viewHolder.btnLike) btnLikeEvent();
                else if (v == viewHolder.btnComment) btnCommentEvent();
                else if (v == viewHolder.btnOption) btnOptionEvent();
                else if (v == viewHolder.tvCountLikes) tvCountLikesEvent();
                else if (v == viewHolder.btnVideo) {
                    try {
                        Intent iOpenUrl = new Intent(Intent.ACTION_VIEW);
                        iOpenUrl.setDataAndType(Uri.parse(mPostedMediaItem.getVideo()), "video/mp4");
                        mContext.startActivity(iOpenUrl);
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "Can't open this video", Toast.LENGTH_SHORT).show();
                    }
                    //Intent iOpenUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(mPostedMediaItem.getVideo()));
                    //mContext.startActivity(iOpenUrl);
                }
            }
        }

        @Override
        public void onLinkClicked(String link) {
            tvCommentsEvent(link);
        }

        @Override
        public void onClicked() {
            tvCommentsEvent(null);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v == viewHolder.btnOption) {
                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    viewHolder.btnOption.setImageAlpha(225);
                } else {
                    viewHolder.btnOption.setImageAlpha(128);
                }
            } else if (v == viewHolder.btnComment) {
                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    viewHolder.btnComment.setImageAlpha(225);
                } else {
                    viewHolder.btnComment.setImageAlpha(128);
                }
            }
            return false;
        }
    }
}
