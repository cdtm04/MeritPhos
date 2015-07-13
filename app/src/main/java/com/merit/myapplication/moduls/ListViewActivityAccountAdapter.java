package com.merit.myapplication.moduls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.activities.MainActivity;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by merit on 7/2/2015.
 */
public class ListViewActivityAccountAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private final Context mContext;
    private ArrayList<Post> mPostedMediaItems;
    private HashMap<Integer, ArrayList<Post>> hashMap = new HashMap<>();
    private RelativeLayout nullLayout;
    private UserProfile mUserProfile;
    private boolean isStickyHeader;
    private EventAcctivityAccount mEventAcctivityAccount;

    public ListViewActivityAccountAdapter(Context context, ArrayList<Post> postedMediaItems, UserProfile mUserProfile, boolean isStickyHeader, EventAcctivityAccount mEventAcctivityAccount) {
        super();
        mContext = context;
        mPostedMediaItems = postedMediaItems;
        this.isStickyHeader = isStickyHeader;
        this.mUserProfile = mUserProfile;
        this.mEventAcctivityAccount = mEventAcctivityAccount;

        // init a null layout
        nullLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams dimensions = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        nullLayout.setLayoutParams(dimensions);

        // init hashMap, change ArrayList postedMediaItems to a hashMap with value is a ArrayList of 3 items from postedMediaItems
        // to show on item listview
        initHashMap();
    }

    private void initHashMap() {
        hashMap.clear();
        if (mPostedMediaItems.size() == 1) {
            ArrayList<Post> arr1 = new ArrayList<>();
            arr1.add(mPostedMediaItems.get(0));
            arr1.add(null);
            arr1.add(null);
            hashMap.put(hashMap.size(), arr1);
        } else if (mPostedMediaItems.size() == 2) {
            ArrayList<Post> arr1 = new ArrayList<>();
            arr1.add(mPostedMediaItems.get(0));
            arr1.add(mPostedMediaItems.get(1));
            arr1.add(null);
            hashMap.put(hashMap.size(), arr1);
        } else {
            for (int i = 0; i <= mPostedMediaItems.size() - 3; i = i + 3) {
                ArrayList<Post> arr = new ArrayList<>();
                arr.add(mPostedMediaItems.get(i));
                arr.add(mPostedMediaItems.get(i + 1));
                arr.add(mPostedMediaItems.get(i + 2));
                hashMap.put(hashMap.size(), arr);
                if (i + 1 == mPostedMediaItems.size() - 3) {
                    ArrayList<Post> arr1 = new ArrayList<>();
                    arr1.add(mPostedMediaItems.get(i + 3));
                    arr1.add(null);
                    arr1.add(null);
                    hashMap.put(hashMap.size(), arr1);
                }
                if (i + 2 == mPostedMediaItems.size() - 3) {
                    ArrayList<Post> arr1 = new ArrayList<>();
                    arr1.add(mPostedMediaItems.get(i + 3));
                    arr1.add(mPostedMediaItems.get(i + 4));
                    arr1.add(null);
                    hashMap.put(hashMap.size(), arr1);
                }
            }
        }
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        // when change view, we need to reset view = null, so we can set other views
        view = null;
        if (isStickyHeader) {
            int type = getItemViewType(i);
            // type = 0, set a null layout in header
            if (type == 0)
                return LayoutInflater.from(mContext).inflate(R.layout.nulllayout, viewGroup, false);
            else return getStickyHeaderView(i - 1, view, viewGroup);
        }
        // if it don't need stickyhear, set a null layout in header
        else return LayoutInflater.from(mContext).inflate(R.layout.nulllayout, viewGroup, false);
    }

    private View getStickyHeaderView(int i, View view, ViewGroup viewGroup) {
        ListViewHeaderHolder holder = null;
        if (view == null) {
            holder = new ListViewHeaderHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.itemheader_listview_activityhome, viewGroup, false);
            holder.ivAvatar = (CircleImageView) view.findViewById(R.id.ivAvatar);
            holder.tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            holder.tvPostedTime = (TextView) view.findViewById(R.id.tvPostedTime);
            holder.btnAccount = (RelativeLayout) view.findViewById(R.id.btnAccount);
            view.setTag(holder);
        } else {
            holder = (ListViewHeaderHolder) view.getTag();
        }

        // set value
        Post mPostedMediaItem = mPostedMediaItems.get(i);
        //holder.ivAvatar.setImageBitmap(mPostedMediaItem.getAvatar());
        new ImageLoader(mContext).DisplayImage(mPostedMediaItem.getUser().getProfilePicture(), holder.ivAvatar);
        holder.tvUsername.setText(mPostedMediaItem.getUser().getUserName());
        holder.tvPostedTime.setText(mPostedMediaItem.getCreatedTime());

        // set event for items
        holder.btnAccount.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, holder, null, view, i));
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        // if isStickHeader we use mPostedMediaItems
        if (isStickyHeader) return mPostedMediaItems.size() + 1;
            // if isStickHeader we use hashMap
        else return hashMap.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) return mUserProfile;
        else {
            if (isStickyHeader) return hashMap.get(position - 1);
            else return mPostedMediaItems.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = null;
        if (isStickyHeader) {
            return getItemViewOfListViewStyle(position, convertView, parent);
        } else {
            return getItemViewOfGridViewStyle(position, convertView, parent);
        }
    }

    private View getItemViewOfListViewMedia(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = null;
        if (convertView == null) {
            holder = new ListViewHolder();
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
            holder = (ListViewHolder) convertView.getTag();
        }

        // set value on items
        Post mPostedMediaItem = mPostedMediaItems.get(position);
        //holder.ivPicture.setImageBitmap(mPostedMediaItem.getPicture());
        new ImageLoader(mContext).DisplayImage(mPostedMediaItem.getMedia().getUrl(), holder.ivPicture);
        holder.btnOption.setImageAlpha(135);
        holder.btnComment.setImageAlpha(135);
        if (mPostedMediaItem.isLiked(mUserProfile.getUserName()))
            holder.btnLike.setImageResource(R.drawable.icon_liked);
        else
            holder.btnLike.setImageResource(R.drawable.icon_like);
        holder.tvCountLikes.setText((mPostedMediaItem.getLikes().size() == 1) ? mPostedMediaItem.getLikes().size() + " like" : mPostedMediaItem.getLikes().size() + " likes");

        // set value textview comments:
        // use this for username format
        SpannedUserNameTextFormat spannedUserNameTextFormat = new SpannedUserNameTextFormat();
        String contentOfTvComments = "<font color='gray'>Add a comment</font>";
        String captionOfUser = "";
        if (mPostedMediaItem.getCaptionOfPost() != null)
            captionOfUser = "(user)" + mPostedMediaItem.getUser().getUserName() + "(/user) " + mPostedMediaItem.getCaptionOfPost().getTextOfCaption() + "<br>";
        if (mPostedMediaItem.getComments().size() > 0) {
            String countComments = "<font color='gray'>View all " + ((mPostedMediaItem.getComments().size() + ((mPostedMediaItem.getComments().size() == 1) ? " comment</font>" : " comments</font>")));
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
        LinkedTextView.autoLink(holder.tvComments, contentOfTvComments, new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        //spannedUserNameTextFormat.setSpannedTextUserName(convertView.getContext(), holder.tvComments, contentOfTvComments);
        //holder.tvComments.setText(contentOfTvComments);

        // set event for items
        holder.btnLike.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnComment.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.tvCountLikes.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        return convertView;
    }

    private View getItemViewProfile(int position, View convertView, ViewGroup parent) {
        ViewHolderProfile viewHolderProfile = null;
        if (convertView == null) {
            viewHolderProfile = new ViewHolderProfile();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemheader_listview_activityaccount, parent, false);

            viewHolderProfile.ivAvatarProfile = (CircleImageView) convertView.findViewById(R.id.ivAvatarProfile);
            viewHolderProfile.btnCountPosts = (RelativeLayout) convertView.findViewById(R.id.btnCountPosts);
            viewHolderProfile.btnCountFollowers = (RelativeLayout) convertView.findViewById(R.id.btnCountFollwers);
            viewHolderProfile.btnCountFollowing = (RelativeLayout) convertView.findViewById(R.id.btnCountFollowing);
            viewHolderProfile.tvCountPosts = (TextView) convertView.findViewById(R.id.tvCountPosts);
            viewHolderProfile.tvCountFollowing = (TextView) convertView.findViewById(R.id.tvCountFollowing);
            viewHolderProfile.tvCountFollowers = (TextView) convertView.findViewById(R.id.tvCountFollowers);
            viewHolderProfile.btnEditProfile = (Button) convertView.findViewById(R.id.btnEditProfile);
            viewHolderProfile.tvFullNameProfile = (TextView) convertView.findViewById(R.id.tvFullNameProfile);
            viewHolderProfile.tvLinkProfile = (TextView) convertView.findViewById(R.id.tvLinkProfile);
            viewHolderProfile.tvBioProfile = (TextView) convertView.findViewById(R.id.tvBioProfile);
            viewHolderProfile.btnGridViewMedia = (ImageView) convertView.findViewById(R.id.btnGridViewMedia);
            viewHolderProfile.btnListViewMedia = (ImageView) convertView.findViewById(R.id.btnListViewMedia);
            viewHolderProfile.btnMapMedia = (ImageView) convertView.findViewById(R.id.btnMapMedia);
            viewHolderProfile.btnTagMedia = (ImageView) convertView.findViewById(R.id.btnTagMedia);

            convertView.setTag(viewHolderProfile);
        } else {
            viewHolderProfile = (ViewHolderProfile) convertView.getTag();
        }

        // set value
        new ImageLoader(mContext).DisplayImage(mUserProfile.getAvatar(), viewHolderProfile.ivAvatarProfile);
        viewHolderProfile.tvCountPosts.setText(mUserProfile.getCountPosts() + "");
        viewHolderProfile.tvCountFollowers.setText(mUserProfile.getCountFollowers() + "");
        viewHolderProfile.tvCountFollowing.setText(mUserProfile.getCountFollowing() + "");
        if (mUserProfile.getFullName().equals("") || mUserProfile.getFullName() == null)
            viewHolderProfile.tvFullNameProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvFullNameProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvFullNameProfile.setText(mUserProfile.getFullName() + "");
        }
        if (mUserProfile.getBio().equals("") || mUserProfile.getBio() == null)
            viewHolderProfile.tvBioProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvBioProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvBioProfile.setText(mUserProfile.getBio() + "");
        }
        if (mUserProfile.getLink().equals("") || mUserProfile.getLink() == null)
            viewHolderProfile.tvLinkProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvLinkProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvLinkProfile.setText(mUserProfile.getLink() + "");
        }

        // change icon
        if (isStickyHeader) {
            viewHolderProfile.btnGridViewMedia.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_photo_gridview_profile_click));
            viewHolderProfile.btnListViewMedia.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_photo_listview_profile));
            viewHolderProfile.btnGridViewMedia.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        } else {
            viewHolderProfile.btnGridViewMedia.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_photo_gridview_profile));
            viewHolderProfile.btnListViewMedia.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_photo_listview_profile_click));
            viewHolderProfile.btnListViewMedia.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        }

        // set event
        viewHolderProfile.ivAvatarProfile.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));


        viewHolderProfile.btnMapMedia.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        viewHolderProfile.btnTagMedia.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        viewHolderProfile.btnCountPosts.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        viewHolderProfile.btnCountFollowers.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        viewHolderProfile.btnCountFollowing.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));
        viewHolderProfile.btnEditProfile.setOnClickListener(new EventProfileItems(viewHolderProfile, convertView));

        return convertView;
    }

    private View getItemViewOfGridViewViewMedia(int position, View convertView, ViewGroup parent) {
        GridViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new GridViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_activityaccount_normal, parent, false);
            viewHolder.media1 = (ImageView) convertView.findViewById(R.id.media1);
            viewHolder.media2 = (ImageView) convertView.findViewById(R.id.media2);
            viewHolder.media3 = (ImageView) convertView.findViewById(R.id.media3);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (GridViewHolder) convertView.getTag();
        }

        // set value and event
        ArrayList<Post> postedMediaItems = hashMap.get(position);
        if (postedMediaItems.get(0) != null) {
            //viewHolder.media1.setImageBitmap(postedMediaItems.get(0).getPicture());
            new ImageLoader(mContext).DisplayImage(postedMediaItems.get(0).getMedia().getUrl(), viewHolder.media1);
            viewHolder.media1.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(0), convertView, position));
        }
        if (postedMediaItems.get(1) != null) {
            //viewHolder.media2.setImageBitmap(postedMediaItems.get(1).getPicture());
            new ImageLoader(mContext).DisplayImage(postedMediaItems.get(1).getMedia().getUrl(), viewHolder.media2);
            viewHolder.media2.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(1), convertView, position));
        }
        if (postedMediaItems.get(2) != null) {
            //viewHolder.media3.setImageBitmap(postedMediaItems.get(2).getPicture());
            new ImageLoader(mContext).DisplayImage(postedMediaItems.get(2).getMedia().getUrl(), viewHolder.media3);
            viewHolder.media3.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(2), convertView, position));
        }
        return convertView;
    }

    private View getItemViewOfListViewStyle(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0)
            return getItemViewProfile(position, convertView, parent);
        else return getItemViewOfListViewMedia(position - 1, convertView, parent);
    }

    private View getItemViewOfGridViewStyle(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0)
            return getItemViewProfile(position, convertView, parent);
        else return getItemViewOfGridViewViewMedia(position - 1, convertView, parent);
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

    private class ListViewHolder {
        ImageView ivPicture;
        ImageView btnLike, btnComment, btnOption;
        TextView tvCountLikes, tvComments;
        RelativeLayout btnLikeCount;
    }

    private class ListViewHeaderHolder {
        RelativeLayout btnAccount;
        CircleImageView ivAvatar;
        TextView tvUsername, tvPostedTime;
    }

    private class GridViewHolder {
        ImageView media1, media2, media3;
    }

    private class ViewHolderProfile {
        CircleImageView ivAvatarProfile;
        RelativeLayout btnCountPosts, btnCountFollowers, btnCountFollowing;
        Button btnEditProfile;
        TextView tvCountPosts, tvCountFollowers, tvCountFollowing, tvFullNameProfile, tvBioProfile, tvLinkProfile;
        ImageView btnGridViewMedia, btnListViewMedia, btnMapMedia, btnTagMedia;
    }

    private class EventStickyHeaderItems implements View.OnClickListener, LinkedTextView.OnClickListener {
        Post mPostedMediaItem;
        ListViewHeaderHolder viewHeaderHolder;
        ListViewHolder viewHolder;
        View view;
        int position;

        public EventStickyHeaderItems(Post mPostedMediaItem, ListViewHeaderHolder viewHeaderHolder, ListViewHolder viewHolder, View view, int position) {
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

    private class EventProfileItems implements View.OnClickListener {
        ViewHolderProfile viewHolderProfile;
        View view;

        public EventProfileItems(ViewHolderProfile viewHolderProfile, View view) {
            this.viewHolderProfile = viewHolderProfile;
            this.view = view;
        }

        // CODE btnEditProfile EVENT
        private void btnEditProfileEvent() {
            // TO DO WHEN CLICK btnEditProfile HERE

            Toast.makeText(view.getContext(), "btnEditProfile is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnCountPosts EVENT
        private void btnCountPostsEvent() {
            // TO DO WHEN CLICK btnCountPosts HERE

            //Toast.makeText(view.getContext(), "btnCountPosts is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnCountFollowing EVENT
        private void btnCountFollowingEvent() {
            // TO DO WHEN CLICK btnCountFollowing HERE

            Toast.makeText(view.getContext(), "btnCountFollowing is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnCountFollwers EVENT
        private void btnCountFollowersEvent() {
            // TO DO WHEN CLICK btnCountFollwers HERE

            Toast.makeText(view.getContext(), "btnCountFollwers is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnTagMedia EVENT
        private void btnTagMediaEvent() {
            // TO DO WHEN CLICK btnTagMedia HERE

            Toast.makeText(view.getContext(), "btnTagMedia is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnMapMedia EVENT
        private void btnMapMediaEvent() {
            // TO DO WHEN CLICK btnMapMedia HERE

            Toast.makeText(view.getContext(), "btnMapMedia is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE ivAvatarProfile EVENT
        private void ivAvatarProfileEvent() {
            // TO DO WHEN CLICK ivAvatarProfile HERE

            Toast.makeText(view.getContext(), "ivAvatarProfile is clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if (v == viewHolderProfile.ivAvatarProfile) ivAvatarProfileEvent();
            else if (v == viewHolderProfile.btnTagMedia) btnTagMediaEvent();
            else if (v == viewHolderProfile.btnMapMedia) btnMapMediaEvent();
            else if (v == viewHolderProfile.btnCountPosts)
                mEventAcctivityAccount.btnCountPostsEvent();
            else if (v == viewHolderProfile.btnCountFollowing) btnCountFollowingEvent();
            else if (v == viewHolderProfile.btnCountFollowers) btnCountFollowersEvent();
            else if (v == viewHolderProfile.btnEditProfile) btnEditProfileEvent();
            else if (v == viewHolderProfile.btnListViewMedia) {
                // change listview to ListViewMedia style
                isStickyHeader = true;
                notifyDataSetChanged();

            } else if (v == viewHolderProfile.btnGridViewMedia) {
                // change listview to ListViewMedia style
                isStickyHeader = false;
                notifyDataSetChanged();
            }
        }
    }


    private class EventGridViewItem implements View.OnClickListener {
        GridViewHolder viewHolder;
        View view;
        int position;
        Post mPostedMediaItem;

        public EventGridViewItem(GridViewHolder viewHolder, Post postedMediaItem, View view, int position) {
            this.viewHolder = viewHolder;
            this.view = view;
            this.position = position;
            this.mPostedMediaItem = postedMediaItem;
        }

        // CODE media3 EVENT
        private void media3Event() {
            // TO DO WHEN CLICK media3 HERE

            Toast.makeText(view.getContext(), "media3 of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE media2 EVENT
        private void media2Event() {
            // TO DO WHEN CLICK media2 HERE

            Toast.makeText(view.getContext(), "media2 of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE media1 EVENT
        private void media1Event() {
            // TO DO WHEN CLICK media1 HERE

            Toast.makeText(view.getContext(), "media1 of user " + mPostedMediaItem.getUser().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if (v == viewHolder.media1) media1Event();
            else if (v == viewHolder.media2) media2Event();
            else if (v == viewHolder.media3) media3Event();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        initHashMap();
        super.notifyDataSetChanged();
    }

    public interface EventAcctivityAccount {
        public void btnCountPostsEvent();
    }

}
