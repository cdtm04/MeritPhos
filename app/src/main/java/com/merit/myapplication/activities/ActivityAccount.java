package com.merit.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.ApplicationData;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.instagram.JSONParser;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.UserProfile;
import com.merit.myapplication.moduls.ActionBar;
import com.merit.myapplication.moduls.ListViewActivityAccountAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by merit on 6/27/2015.
 */
public class ActivityAccount extends FragmentActivity implements ListViewActivityAccountAdapter.EventAcctivityAccount {
    private String mUserId;
    private String mParentActivity;

    private ActionBar abAccount;
    private SwipeRefreshLayout swipeRefreshActivityAccount;
    private StickyListHeadersListView lvProfile;
    private ListViewActivityAccountAdapter adapter;

    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private UserProfile userProfile;
    private ArrayList<Post> userMedias = new ArrayList<>();

    // for the back Button event
    private int mPositionInGridView = 0;

    private boolean mIsLoadingUserProfile = false;
    private boolean mIsLoadingUserMedias = false;
    private boolean mIsLoadingNextUserMedias = false;
    private boolean mIsGridViewItemCliked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialize();
        if (!mIsLoadingUserProfile) {
            loadUserProfile();
        }

    }

    private void initialize() {

        final Intent intent = getIntent();

        mParentActivity = intent.getStringExtra("PARENT");
        if (mParentActivity == null) {
            mParentActivity = "AccountActivity";
        }

        mUserId = intent.getStringExtra("ID");
        if (mUserId == null) {
            mUserId = InstagramApp.instagramApp.getId();
        }

        abAccount = (ActionBar) findViewById(R.id.abAccount);
        abAccount.setLabelName("ACCOUNT");
        if (mParentActivity.startsWith("AccountActivity") && mUserId.equals(InstagramApp.instagramApp.getId())) {
            abAccount.setButtonLeftEnabled(false);
            abAccount.setSeparatorEnabled(false);
            // init actionbar
            abAccount.setOnActionBarListener(new ActionBar.OnActionBarListener() {
                @Override
                public void onButtonLeftClick(View v) {
                    if (mIsGridViewItemCliked) {
                        adapter.changeViewStyle(false);
                        adapter.changeStateOfViewProfile(true);
                        abAccount.setButtonLeftEnabled(false);
                        abAccount.setSeparatorEnabled(false);
                        abAccount.setButtonRightEnabled(true);
                        lvProfile.setSelection(mPositionInGridView);
                        mIsGridViewItemCliked = false;
                    } else {
                        back();
                    }
                }

                @Override
                public void onButtonRightClick(View v) {
                    Intent i = new Intent(v.getContext(), ActivityOptions.class);
                    View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityOptions", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                    ActivityAccountGroup.groupAccountGroup.replaceView(view);
                }
            });
        } else {
            abAccount.setButtonLeftEnabled(true);
            abAccount.setButtonRightEnabled(false);
            abAccount.setSeparatorEnabled(true);
            abAccount.setOnActionBarListener(new ActionBar.OnActionBarListener() {
                @Override
                public void onButtonLeftClick(View v) {
                    if (mIsGridViewItemCliked) {
                        adapter.changeViewStyle(false);
                        adapter.changeStateOfViewProfile(true);
                        lvProfile.setSelection(mPositionInGridView);
                        mIsGridViewItemCliked = false;
                    } else {
                        back();
                    }
                }

                @Override
                public void onButtonRightClick(View v) {
                }
            });
        }

        // example data
        userProfile = new UserProfile(0, 0, 0, null, null, null, null, null, null, null, null);
        // adapter for listview
        adapter = new ListViewActivityAccountAdapter(this, userMedias, userProfile, false, this);

        // init lvProfile
        lvProfile = (StickyListHeadersListView) findViewById(R.id.lvProfile);
        lvProfile.setAreHeadersSticky(true);
        lvProfile.setAdapter(adapter);
        lvProfile.setClickable(true);
        lvProfile.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (lvProfile.getLastVisiblePosition() - lvProfile.getHeaderViewsCount() -
                        lvProfile.getFooterViewsCount()) >= (adapter.getCount() - 1)) {
                    if (!mIsLoadingNextUserMedias) {
                        loadNextPageUserMedias();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        // init swipeRefreshActivityAccount
        swipeRefreshActivityAccount = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshActivityAccount);
        swipeRefreshActivityAccount.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshActivityAccountEvent();
                swipeRefreshActivityAccount.setRefreshing(false);// stop refreshing
            }
        });
        swipeRefreshActivityAccount.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void loadUserProfile() {
        mIsLoadingUserProfile = true;
        InstagramApp.instagramApp.fetchProfileInformation(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    userInfoHashmap = InstagramApp.instagramApp.getUserInfo();

                    // update user information
                    String id = userInfoHashmap.get(InstagramApp.TAG_ID);
                    String userName = userInfoHashmap.get(InstagramApp.TAG_USERNAME);
                    int countPosts = Integer.parseInt(userInfoHashmap.get(InstagramApp.TAG_MEDIA));
                    int countFollowers = Integer.parseInt(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
                    int countFollowing = Integer.parseInt(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
                    String fullName = userInfoHashmap.get(InstagramApp.TAG_FULL_NAME);
                    String link = userInfoHashmap.get(InstagramApp.TAG_WEBSITE);
                    String bio = userInfoHashmap.get(InstagramApp.TAG_BIO);
                    String avatar = userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE);
                    String incoming = userInfoHashmap.get(InstagramApp.TAG_INCOMING);
                    String outgoing = userInfoHashmap.get(InstagramApp.TAG_OUTGOING);
                    if (userName.equals(InstagramApp.instagramApp.getUserName())) {
                        incoming = "me";
                        outgoing = "me";
                    }

                    userProfile.setValues(id, userName, countPosts, countFollowers, countFollowing, fullName, link, bio, avatar, incoming, outgoing);

                    adapter.notifyDataSetChanged();

                    // load medias' user
                    if (!mIsLoadingUserMedias) {
                        loadUserMedias();
                    }

                    // update title acctionbar
                    abAccount.setLabelName(userProfile.getUserName().toUpperCase());
                } else if (msg.what == InstagramApp.WHAT_ERROR) {
                    Toast.makeText(MainActivity.mainContext, "Can't get this user's info. Maybe it's private", Toast.LENGTH_SHORT).show();
                    lvProfile.setClickable(false);
                }
                mIsLoadingUserProfile = false;
                return false;
            }
        }), mUserId);
    }

    private void loadUserMedias() {
        mIsLoadingUserMedias = true;
        mIsLoadingNextUserMedias = false;
        InstagramApp.instagramApp.fetchAllRecentMedias(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    userMedias.clear();
                    ArrayList<Post> mPosts = InstagramApp.instagramApp.getAllRecentMedias();
                    for (int i = 0; i < mPosts.size(); i++) {
                        userMedias.add(mPosts.get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else if (msg.what == InstagramApp.WHAT_ERROR) {
                    Toast.makeText(MainActivity.mainContext, "Can't get this user's info.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.mainContext, "Try to refresh.", Toast.LENGTH_SHORT).show();
                }
                mIsLoadingUserMedias = false;
                return false;
            }
        }), mUserId);
    }

    private void loadNextPageUserMedias() {
        mIsLoadingNextUserMedias = true;
        InstagramApp.instagramApp.fetchNextPageAllRecentMedias(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    userMedias.clear();

                    ArrayList<Post> mPosts = InstagramApp.instagramApp.getAllRecentMedias();

                    for (int i = 0; i < mPosts.size(); i++) {
                        userMedias.add(mPosts.get(i));
                    }

                    adapter.notifyDataSetChanged();
                    mIsLoadingNextUserMedias = false;
                } else {
                    Toast.makeText(ActivityAccount.this, "No more data.", Toast.LENGTH_SHORT).show();
                    mIsLoadingNextUserMedias = true;
                }
                return false;
            }
        }));
    }

    // CODE swipeRefreshActivityAccount EVENT
    private void swipeRefreshActivityAccountEvent() {
        if (!mIsLoadingUserProfile) {
            loadUserProfile();
        }
        if (!mIsLoadingUserMedias) {
            loadUserMedias();
        }
    }


    @Override
    public void btnCountPostsEvent() {
        lvProfile.postDelayed(new Runnable() {
            @Override
            public void run() {
                // converting pixels to dps
                float scaleValue = MainActivity.mainContext.getResources().getDisplayMetrics().density;
                int pixels = (int) (50 * scaleValue + 0.5f);
                lvProfile.smoothScrollToPositionFromTop(1, pixels, 300);
            }
        }, 100L);
    }

    @Override
    public void onClickGridViewItem(int positionInGridView, int positonInListView) {
        // change view style to listview and scroll it to position
        mPositionInGridView = positionInGridView;
        final int mPositionInListView = positonInListView;
        lvProfile.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsGridViewItemCliked = true;
                adapter.changeViewStyle(true);
                adapter.changeStateOfViewProfile(false);
                // enable left button off actionbar, give the quick return to profile screen
                abAccount.setButtonLeftEnabled(true);
                abAccount.setSeparatorEnabled(true);
                abAccount.setButtonRightEnabled(false);
                lvProfile.setSelection(mPositionInListView);
            }
        }, 100L);
    }

    private void startRelationshopActivity(String label) {
        Intent iOpenRelationship = new Intent(this, ActivityRelationship.class);
        iOpenRelationship.putExtra("LABELNAME", label);
        iOpenRelationship.putExtra("ID", userProfile.getId());
        iOpenRelationship.putExtra("PARENT", mParentActivity);
        if (mParentActivity.startsWith("AccountActivity")) {
            View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityRelationship", iOpenRelationship.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityAccountGroup.groupAccountGroup.replaceView(view);
        } else if (mParentActivity.startsWith("HomeActivity")) {
            View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityRelationship", iOpenRelationship.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityHomeGroup.groupHomeGroup.replaceView(view);
        }
    }

    @Override
    public void btnCountFollowersEvent() {
        if (lvProfile.isClickable()) startRelationshopActivity(InstagramApp.GET_FOLLOWERS);
    }

    @Override
    public void btnCountFollowingEvent() {
        if (lvProfile.isClickable()) startRelationshopActivity(InstagramApp.GET_FOLLOWING);
    }

    @Override
    public void tvCommentsEventOnText(Post mPostedMediaItem) {
        Intent intent = new Intent(this, ActivityComment.class);
        intent.putExtra(MainActivity.PARENT, MainActivity.PARENT_ACCOUNT);
        intent.putExtra("MEDIAID", mPostedMediaItem.getId());
        intent.putExtra("USERNAME", mPostedMediaItem.getUserOfPost().getUserName());
        intent.putExtra("USERID", mPostedMediaItem.getUserOfPost().getId());
        intent.putExtra("AVATAR", mPostedMediaItem.getUserOfPost().getProfilePicture());
        if (mPostedMediaItem.getCaptionOfPost() != null) {
            intent.putExtra("TEXT", mPostedMediaItem.getCaptionOfPost().getTextOfCaption());
            intent.putExtra("TIME", mPostedMediaItem.getCaptionOfPost().getCreatedTimeOfCaption());
        }
        this.startActivity(intent);
    }

    @Override
    public void tvCommentsEventOnLink(String link, Post mPostedMediaItem) {
        Intent iOpenUserInfoActivity = new Intent(this, ActivityAccount.class);
        iOpenUserInfoActivity.putExtra("POSITION", lvProfile.getFirstVisiblePosition());

        String userId = mPostedMediaItem.getUserOfPost().getId();
        for (int i = 0; i < mPostedMediaItem.getComments().size(); i++) {
            if (mPostedMediaItem.getComments().get(i).getUser().getUserName().equals(link)) {
                userId = mPostedMediaItem.getComments().get(i).getUser().getId();
                break;
            }
        }

        if (userId.equals(InstagramApp.instagramApp.getId()) && userId.equals(mUserId)) {
            adapter.changeViewStyle(false);
            adapter.changeStateOfViewProfile(true);
            abAccount.setButtonLeftEnabled(false);
            abAccount.setSeparatorEnabled(false);
            abAccount.setButtonRightEnabled(true);
            lvProfile.setSelection(0);
            mIsGridViewItemCliked = false;
        } else if (userId.equals(mUserId)) {
            adapter.changeViewStyle(false);
            adapter.changeStateOfViewProfile(true);
            abAccount.setButtonLeftEnabled(true);
            abAccount.setSeparatorEnabled(true);
            abAccount.setButtonRightEnabled(false);
            lvProfile.setSelection(0);
            mIsGridViewItemCliked = false;
        } else if (mParentActivity.startsWith("AccountActivity")) {
            iOpenUserInfoActivity.putExtra("ID", userId);
            iOpenUserInfoActivity.putExtra("PARENT", "AccountActivity");
            View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityAccount", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityAccountGroup.groupAccountGroup.replaceView(view);
        } else if (mParentActivity.startsWith("HomeActivity")) {
            iOpenUserInfoActivity.putExtra("ID", userId);
            iOpenUserInfoActivity.putExtra("PARENT", mParentActivity);
            View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("HomeActivity", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityHomeGroup.groupHomeGroup.replaceView(view);
        }

    }


    private void back() {
        if (mParentActivity.startsWith("AccountActivity")) {
            ActivityAccountGroup.groupAccountGroup.back();
        } else if (mParentActivity.startsWith("HomeActivity")) {
            ActivityHomeGroup.groupHomeGroup.back();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
