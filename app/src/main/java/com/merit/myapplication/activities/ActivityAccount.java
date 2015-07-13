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
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.ApplicationData;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.UserProfile;
import com.merit.myapplication.moduls.ActionBar;
import com.merit.myapplication.moduls.ListViewActivityAccountAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by merit on 6/27/2015.
 */
public class ActivityAccount extends FragmentActivity implements ListViewActivityAccountAdapter.EventAcctivityAccount {
    private ActionBar abAccount;
    private SwipeRefreshLayout swipeRefreshActivityAccount;
    private StickyListHeadersListView lvProfile;
    private ListViewActivityAccountAdapter adapter;

    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private UserProfile userProfile;
    private ArrayList<Post> userMedias = new ArrayList<>();

    // for the back Button event
    private int mPositionInGridView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialize();
        loadUserProfile();
        loadUserMedias();

    }

    private void initialize() {

        // init actionbar
        abAccount = (ActionBar) findViewById(R.id.abAccount);
        abAccount.setButtonLeftEnabled(false);
        abAccount.setSeparatorEnabled(false);
        abAccount.setLabelName("ACCOUNT");
        abAccount.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
                adapter.changeViewStyle(false);
                adapter.changeStateOfViewProfile(true);
                abAccount.setButtonLeftEnabled(false);
                abAccount.setSeparatorEnabled(false);
                abAccount.setButtonRightEnabled(true);
                lvProfile.setSelection(mPositionInGridView);
            }

            @Override
            public void onButtonRightClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityOptions.class);
                View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityAccount", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                ActivityAccountGroup.groupAccountGroup.replaceView(view);
            }
        });

        // example data
        userProfile = new UserProfile(null, null, 0, 0, 0, null, null, null, null);
        // adapter for listview
        adapter = new ListViewActivityAccountAdapter(this, userMedias, userProfile, false, this);

        // init lvProfile
        lvProfile = (StickyListHeadersListView) findViewById(R.id.lvProfile);
        lvProfile.setAreHeadersSticky(true);
        lvProfile.setAdapter(adapter);

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
                    userProfile.setValues(id, userName, countPosts, countFollowers, countFollowing, fullName, link, bio, avatar);

                    adapter.notifyDataSetChanged();

                    // update title acctionbar
                    abAccount.setLabelName(userProfile.getUserName().toUpperCase());
                } else if (msg.what == InstagramApp.WHAT_ERROR) {
                    Toast.makeText(MainActivity.mainContext, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }));
    }

    private void loadUserMedias() {
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
                } else {
                    Toast.makeText(MainActivity.mainContext, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }));
    }

    // CODE swipeRefreshActivityAccount EVENT
    private void swipeRefreshActivityAccountEvent() {
        loadUserProfile();
        loadUserMedias();
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


    @Override
    public void onBackPressed() {
        ActivityAccountGroup.groupAccountGroup.back();
    }
}
