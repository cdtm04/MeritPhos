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
    private ProgressDialog mProgressDialog;

    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private UserProfile userProfile;
    private ArrayList<Post> posts = new ArrayList<>();
    private Handler handlerUserInfo;
    private Handler handlerUserMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialize();
    }

    private void initialize() {
        // start Progress dialog
        mProgressDialog = new ProgressDialog(MainActivity.mainContext);
        mProgressDialog.show();

        // init actionbar
        abAccount = (ActionBar) findViewById(R.id.abAccount);
        abAccount.setButtonLeftEnabled(false);
        abAccount.setSeparatorEnabled(false);
        abAccount.setLabelName("ACCOUNT");
        abAccount.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
            }

            @Override
            public void onButtonRightClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityOptions.class);
                View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityAccount", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                ActivityAccountGroup.groupAccountGroup.replaceView(view);
            }
        });

        // exmaple
        posts = new ArrayList<>();
        ArrayList<String> accounts1 = new ArrayList<>();
        accounts1.add("meritfgbhdf");
        accounts1.add("fuckbb");
        accounts1.add("bitchb");
        accounts1.add("wtf");
        ArrayList<String> accounts2 = new ArrayList<>();
        accounts2.add("merit");
        accounts2.add("fuckbb");
        accounts2.add("bitch");
        ArrayList<String> accounts3 = new ArrayList<>();
        accounts3.add("merit");
        accounts3.add("fuck");
        final Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        userProfile = new UserProfile("", "", 0, 0, 0, "", "", "", "");

        // set adapter for listview
        adapter = new ListViewActivityAccountAdapter(this, posts, userProfile, false, this);
        // init lvProfile
        lvProfile = (StickyListHeadersListView) findViewById(R.id.lvProfile);
        lvProfile.setAreHeadersSticky(true);
        lvProfile.setAdapter(adapter);

        handlerUserMedia = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    posts.clear();
                    ArrayList<Post> mPosts = InstagramApp.instagramApp.getPosts();
                    for (int i = 0; i < mPosts.size(); i++) {
                        posts.add(mPosts.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.mainContext, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        handlerUserInfo = new Handler(new Handler.Callback() {
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
        });

        InstagramApp.instagramApp.getAllMediaImages(handlerUserMedia);
        InstagramApp.instagramApp.fetchUserName(handlerUserInfo);


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

    // CODE swipeRefreshActivityAccount EVENT
    private void swipeRefreshActivityAccountEvent() {
        // TO DO: WHEN REFRESHING LAYOUT
        InstagramApp.instagramApp.fetchUserName(handlerUserInfo);
    }

    @Override
    public void onBackPressed() {
        ActivityAccountGroup.groupAccountGroup.back();
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
}
