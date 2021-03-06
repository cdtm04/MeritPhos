package com.merit.myapplication.moduls;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.merit.myapplication.activities.ActivityAccountGroup;
import com.merit.myapplication.activities.ActivityComment;
import com.merit.myapplication.activities.ActivityHomeGroup;
import com.merit.myapplication.activities.ActivityRelationship;
import com.merit.myapplication.activities.MainActivity;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

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

    private ImageLoader mImageLoader;

    public ListViewActivityAccountAdapter(Context context, ArrayList<Post> postedMediaItems, UserProfile mUserProfile, boolean isStickyHeader, EventAcctivityAccount mEventAcctivityAccount) {
        super();
        mContext = context;
        mPostedMediaItems = postedMediaItems;
        this.isStickyHeader = isStickyHeader;
        this.mUserProfile = mUserProfile;
        this.mEventAcctivityAccount = mEventAcctivityAccount;
        mImageLoader = new ImageLoader(mContext);

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

    public void changeViewStyle(boolean isStickyHeaderList) {
        if (isStickyHeaderList) {
            isStickyHeader = true;
            notifyDataSetChanged();
        } else {
            isStickyHeader = false;
            notifyDataSetChanged();
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
        if (mPostedMediaItem.getUserOfPost().getProfilePicture() != null)
            mImageLoader.DisplayImage(mPostedMediaItem.getUserOfPost().getProfilePicture(), holder.ivAvatar);
        if (mPostedMediaItem.getUserOfPost().getUserName() != null)
            holder.tvUsername.setText(mPostedMediaItem.getUserOfPost().getUserName());
        if (mPostedMediaItem.getCreatedTime() != null)
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
            holder.btnVideo = (ImageView) convertView.findViewById(R.id.btnVideo);

            convertView.setTag(holder);

        } else {
            holder = (ListViewHolder) convertView.getTag();
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
            holder.btnVideo.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
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
        LinkedTextView.autoLink(holder.tvComments, contentOfTvComments, new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));

        // set event for items
        holder.btnLike.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnComment.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.tvCountLikes.setOnClickListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnComment.setOnTouchListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
        holder.btnOption.setOnTouchListener(new EventStickyHeaderItems(mPostedMediaItem, null, holder, convertView, position));
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
            viewHolderProfile.btnEditProfile = (ImageView) convertView.findViewById(R.id.btnEditProfile);
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
        if (mUserProfile.getAvatar() != null)
            mImageLoader.DisplayImage(mUserProfile.getAvatar(), viewHolderProfile.ivAvatarProfile);
        viewHolderProfile.tvCountPosts.setText(mUserProfile.getCountPosts() + "");
        viewHolderProfile.tvCountFollowers.setText(mUserProfile.getCountFollowers() + "");
        viewHolderProfile.tvCountFollowing.setText(mUserProfile.getCountFollowing() + "");
        if (mUserProfile.getFullName() == null || mUserProfile.getFullName().equals(""))
            viewHolderProfile.tvFullNameProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvFullNameProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvFullNameProfile.setText(mUserProfile.getFullName() + "");
        }
        if (mUserProfile.getBio() == null || mUserProfile.getBio().equals(""))
            viewHolderProfile.tvBioProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvBioProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvBioProfile.setText(mUserProfile.getBio() + "");
        }
        if (mUserProfile.getLink() == null || mUserProfile.getLink().equals(""))
            viewHolderProfile.tvLinkProfile.setVisibility(View.GONE);
        else {
            viewHolderProfile.tvLinkProfile.setVisibility(View.VISIBLE);
            viewHolderProfile.tvLinkProfile.setText(mUserProfile.getLink() + "");
        }

        if (mUserProfile.getOutgoing() != null) {
            if (mUserProfile.getOutgoing().equals("me")) {

            } else if (mUserProfile.getOutgoing().equals("follows")) {
                viewHolderProfile.btnEditProfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_followed_long2));
            } else {
                viewHolderProfile.btnEditProfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_follow_long2));
            }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridviewstyle_activityaccount, parent, false);
            viewHolder.media1 = (ImageView) convertView.findViewById(R.id.media1);
            viewHolder.media2 = (ImageView) convertView.findViewById(R.id.media2);
            viewHolder.media3 = (ImageView) convertView.findViewById(R.id.media3);
            viewHolder.btnVideo1 = (ImageView) convertView.findViewById(R.id.btnVideo1);
            viewHolder.btnVideo2 = (ImageView) convertView.findViewById(R.id.btnVideo2);
            viewHolder.btnVideo3 = (ImageView) convertView.findViewById(R.id.btnVideo3);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GridViewHolder) convertView.getTag();
        }

        // set value and event
        ArrayList<Post> postedMediaItems = hashMap.get(position);
        if (postedMediaItems.get(0) != null) {
            //viewHolder.media1.setImageBitmap(postedMediaItems.get(0).getPicture());
            mImageLoader.DisplayImage(postedMediaItems.get(0).getImage().getUrl(), viewHolder.media1);
            viewHolder.media1.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(0), convertView, position));
            if (postedMediaItems.get(0).getType().equals("video"))
                viewHolder.btnVideo1.setVisibility(View.VISIBLE);
        }
        if (postedMediaItems.get(1) != null) {
            //viewHolder.media2.setImageBitmap(postedMediaItems.get(1).getPicture());
            mImageLoader.DisplayImage(postedMediaItems.get(1).getImage().getUrl(), viewHolder.media2);
            viewHolder.media2.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(1), convertView, position));
            if (postedMediaItems.get(1).getType().equals("video"))
                viewHolder.btnVideo2.setVisibility(View.VISIBLE);
        }
        if (postedMediaItems.get(2) != null) {
            //viewHolder.media3.setImageBitmap(postedMediaItems.get(2).getPicture());
            mImageLoader.DisplayImage(postedMediaItems.get(2).getImage().getUrl(), viewHolder.media3);
            viewHolder.media3.setOnClickListener(new EventGridViewItem(viewHolder, postedMediaItems.get(2), convertView, position));
            if (postedMediaItems.get(2).getType().equals("video"))
                viewHolder.btnVideo3.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private boolean mHasViewProfile = true;

    public void changeStateOfViewProfile(boolean hasViewProfile) {
        if (hasViewProfile) mHasViewProfile = true;
        else mHasViewProfile = false;
        notifyDataSetChanged();
    }

    private View getItemViewOfListViewStyle(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            if (mHasViewProfile) return getItemViewProfile(position, convertView, parent);
            else return LayoutInflater.from(mContext).inflate(R.layout.nulllayout, parent, false);
        } else return getItemViewOfListViewMedia(position - 1, convertView, parent);
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
        ImageView btnLike, btnComment, btnOption, btnVideo;
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
        ImageView btnVideo1, btnVideo2, btnVideo3;
    }

    private class ViewHolderProfile {
        CircleImageView ivAvatarProfile;
        RelativeLayout btnCountPosts, btnCountFollowers, btnCountFollowing;
        TextView tvCountPosts, tvCountFollowers, tvCountFollowing, tvFullNameProfile, tvBioProfile, tvLinkProfile;
        ImageView btnGridViewMedia, btnListViewMedia, btnMapMedia, btnTagMedia, btnEditProfile;
    }

    private class EventStickyHeaderItems implements View.OnClickListener, LinkedTextView.OnClickListener, View.OnTouchListener {
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
                mEventAcctivityAccount.tvCommentsEventOnLink(link, mPostedMediaItem);

                //Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on link", Toast.LENGTH_SHORT).show();
            } else {
                // TO DO WHEN CLICK tvComments on TEXVIEW HERE
                mEventAcctivityAccount.tvCommentsEventOnText(mPostedMediaItem);
                //Toast.makeText(mContext, "tvComments " + position + " of user " + link + " is clicked on text", Toast.LENGTH_SHORT).show();
            }
        }

        // CODE tvCountLikes EVENT

        private void tvCountLikesEvent() {
            // TO DO WHEN CLICK tvCountLikes HERE
            Intent iOpenRelationship = new Intent(mContext, ActivityRelationship.class);
            iOpenRelationship.putExtra("LABELNAME", InstagramApp.GET_LIKED_USERS);
            iOpenRelationship.putExtra("ID", mPostedMediaItem.getId());
            iOpenRelationship.putExtra(MainActivity.PARENT, MainActivity.PARENT_ACCOUNT);
            View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityRelationship", iOpenRelationship.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityAccountGroup.groupAccountGroup.replaceView(view);

            //Toast.makeText(mContext, "tvCountLikes " + position + " of user " + mPostedMediaItem.getUserOfPost().getUserName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        // CODE btnOption EVENT
        private void btnOptionEvent() {
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
            else if (v == viewHolderProfile.btnCountFollowing)
                mEventAcctivityAccount.btnCountFollowingEvent();
            else if (v == viewHolderProfile.btnCountFollowers)
                mEventAcctivityAccount.btnCountFollowersEvent();
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

        @Override
        public void onClick(View v) {
            int positonInListView = 0;
            for (int i = 0; i < mPostedMediaItems.size(); i++) {
                if (mPostedMediaItem.getId().equals(mPostedMediaItems.get(i).getId())) {
                    positonInListView = i + 1;
                    break;
                }
            }
            mEventAcctivityAccount.onClickGridViewItem(position + 1, positonInListView);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        initHashMap();
        super.notifyDataSetChanged();
    }

    public interface EventAcctivityAccount {
        public void btnCountPostsEvent();

        public void onClickGridViewItem(int positionInGridView, int positonInListView);

        public void btnCountFollowersEvent();

        public void btnCountFollowingEvent();

        public void tvCommentsEventOnText(Post mPostedMediaItem);

        public void tvCommentsEventOnLink(String link, Post mPostedMediaItem);
    }
}
