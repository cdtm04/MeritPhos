package com.merit.myapplication.moduls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Post;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by merit on 7/2/2015.
 */
public class ListViewActivityHomeAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private final Context mContext;
    private ArrayList<Post> mPostedMediaItems;

    public ListViewActivityHomeAdapter(Context context, ArrayList<Post> postedMediaItems) {
        mContext = context;
        mPostedMediaItems = postedMediaItems;
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
        //holder.ivAvatar.setImageBitmap(mPostedMediaItem.getAvatar());
        //holder.tvUsername.setText(mPostedMediaItem.getUsername());
        // holder.tvPostedTime.setText(mPostedMediaItem.getPostedTime());

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
            // zoom image fit the screen
            holder.ivPicture.setAdjustViewBounds(true);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set value on items
        Post mPostedMediaItem = mPostedMediaItems.get(position);
        //holder.ivPicture.setImageBitmap(mPostedMediaItem.getPicture());
        new ImageLoader(mContext).DisplayImage(mPostedMediaItem.getMedia().getUrl(), holder.ivPicture);
        holder.btnOption.setImageAlpha(135);
        holder.btnComment.setImageAlpha(135);
        if (mPostedMediaItem.isLiked("cdtm04"))
            holder.btnLike.setImageResource(R.drawable.icon_liked);
        else
            holder.btnLike.setImageResource(R.drawable.icon_like);
        holder.tvCountLikes.setText((mPostedMediaItem.getLikes().size() == 1) ? mPostedMediaItem.getLikes().size() + " like" : mPostedMediaItem.getLikes().size() + " likes");

        // set value textview comments:
        // use this for username format
        SpannedUserNameTextFormat spannedUserNameTextFormat = new SpannedUserNameTextFormat();
        String contentOfTvComments = "<font color='gray'>Add a comment</font>";
        if (mPostedMediaItem.getComments().size() > 0) {
            String captionOfUser = "";
            String countComments = "<font color='gray'>View all " + ((mPostedMediaItem.getComments().size() + ((mPostedMediaItem.getComments().size() == 1) ? " comment</font>" : " comments</font>")));
            String comments = "<br>";
            for (int i = 0; i < mPostedMediaItem.getComments().size(); i++) {
                String account = mPostedMediaItem.getComments().get(i).getUser().getUserName();
                String comment = mPostedMediaItem.getComments().get(i).getText();
                // get 3 comments
                if (i <= 2)
                    comments += "(user)" + account + "(/user) " + comment + "<br>";
                // get caption from user
                if (mPostedMediaItem.getUser().getUserName().equals(mPostedMediaItem.getComments().get(i).getUser().getUserName()))
                    captionOfUser = "(user)" + account + "(/user) " + comment + "<br>";
            }
            contentOfTvComments = captionOfUser + countComments + comments + "<font color='gray'>Add a comment</font>";
        }
        // set text & event for tvComments
        LinkedTextView.autoLink(holder.tvComments, contentOfTvComments, new EventItems(mPostedMediaItem, null, holder, convertView, position));
        //spannedUserNameTextFormat.setSpannedTextUserName(convertView.getContext(), holder.tvComments, contentOfTvComments);
        //holder.tvComments.setText(contentOfTvComments);

        // set event for items
        holder.btnLike.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnComment.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));
        holder.tvCountLikes.setOnClickListener(new EventItems(mPostedMediaItem, null, holder, convertView, position));

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

    private class ViewHolder {
        ImageView ivPicture;
        ImageView btnLike, btnComment, btnOption;
        TextView tvCountLikes, tvComments;
        RelativeLayout btnLikeCount;
    }

    private class ViewHeaderHolder {
        RelativeLayout btnAccount;
        CircleImageView ivAvatar;
        TextView tvUsername, tvPostedTime;
    }

    private class EventItems implements View.OnClickListener, LinkedTextView.OnClickListener {
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

                Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on link", Toast.LENGTH_SHORT).show();
            } else {
                // TO DO WHEN CLICK tvComments on TEXVIEW HERE

                Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on text", Toast.LENGTH_SHORT).show();
            }
        }

        // CODE tvCountLikes EVENT

        private void tvCountLikesEvent() {
            // TO DO WHEN CLICK tvCountLikes HERE

            Toast.makeText(mContext, "tvCountLikes " + position + " of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnOption EVENT
        private void btnOptionEvent() {
            viewHolder.btnOption.setImageAlpha(225);
            // TO DO WHEN CLICK btnOption HERE

            Toast.makeText(mContext, "btnOption " + position + " of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnComment EVENT
        private void btnCommentEvent() {
            viewHolder.btnComment.setImageAlpha(225);
            // TO DO WHEN CLICK btnComment HERE

            Toast.makeText(mContext, "btnComment " + position + " of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnAccount EVENT
        private void btnLikeEvent() {
            // TO DO WHEN CLICK btnLike HERE
            // reset icon btnLike and update likeCount


            Toast.makeText(mContext, "btnLike " + position + " of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnAccount EVENT
        private void btnAccountEvent() {
            // TO DO WHEN CLICK btnAccount HERE

            Toast.makeText(mContext, "btnAccount " + position + " of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
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
    }
}
