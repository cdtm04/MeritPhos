package com.merit.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.models.User;
import com.merit.myapplication.moduls.ActionBar;
import com.merit.myapplication.moduls.ListViewActivityAccountAdapter;
import com.merit.myapplication.moduls.ListViewActivityRelationshipAdapter;

import java.util.ArrayList;

/**
 * Created by merit on 7/14/2015.
 */
public class ActivityRelationship extends Activity {
    private String mUserId;
    private String mParentActivity;
    private String mMediaId;

    ActionBar abRelationship;
    SwipeRefreshLayout swipeRefreshRelationship;
    ListView lvRelationship;
    String labelName = "RELATIONSHIP";
    String getWhat;

    ListViewActivityRelationshipAdapter adapter;
    private ArrayList<User> mUsers = new ArrayList<>();

    private boolean mIsLoadingUsers = false;
    private boolean mIsLoadingNextUsers = false;
    private boolean mIsLoadingLikedUsers = false;
    private boolean mIsLoadingNextLikedUsers = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        initialize();
        if (!mIsLoadingUsers && !getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
            loadUsers();
        } else if (getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
            loadLikedUsers();
        }
    }

    private void initialize() {

        Intent intent = getIntent();
        mUserId = intent.getStringExtra("ID");
        if (mUserId == null) {
            mUserId = InstagramApp.instagramApp.getId();
        }

        mParentActivity = intent.getStringExtra("PARENT");

        // get label name from Activity Account
        getWhat = intent.getStringExtra("LABELNAME");
        if (getWhat.equals(InstagramApp.GET_FOLLOWERS)) labelName = "FOLLOWERS";
        else if (getWhat.equals(InstagramApp.GET_FOLLOWING)) labelName = "FOLLOWING";
        else if (getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
            mMediaId = intent.getStringExtra("ID");
            labelName = "LIKES";
        }

        // init abRelationship
        abRelationship = (ActionBar) findViewById(R.id.abRelationship);
        abRelationship.setLabelName(labelName);
        abRelationship.setButtonRightEnabled(false);
        abRelationship.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
                back();
            }

            @Override
            public void onButtonRightClick(View v) {

            }
        });

        // init swipeRefreshRelationship
        swipeRefreshRelationship = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshRelationship);
        swipeRefreshRelationship.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsLoadingUsers && !getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
                    loadUsers();
                }
                if (!mIsLoadingLikedUsers && getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
                    loadLikedUsers();
                }
                swipeRefreshRelationship.setRefreshing(false);// stop refreshing
            }
        });
        swipeRefreshRelationship.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //init listView
        adapter = new ListViewActivityRelationshipAdapter(this, mUsers);
        lvRelationship = (ListView) findViewById(R.id.lvRelationship);
        lvRelationship.setAdapter(adapter);
        lvRelationship.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (lvRelationship.getLastVisiblePosition() - lvRelationship.getHeaderViewsCount() -
                        lvRelationship.getFooterViewsCount()) >= (adapter.getCount() - 1)) {
                    if (!mIsLoadingNextUsers && !getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
                        loadNextUsers();
                    }
                    if (!mIsLoadingNextLikedUsers && getWhat.equals(InstagramApp.GET_LIKED_USERS)) {
                        loadNextLikedUsers();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lvRelationship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent iOpenUserInfoActivity = new Intent(ActivityRelationship.this, ActivityAccount.class);
                iOpenUserInfoActivity.putExtra("ID", mUsers.get(position).getId());
                iOpenUserInfoActivity.putExtra("PARENT", mParentActivity);
                iOpenUserInfoActivity.putExtra("POSITION", position);
                if (mParentActivity.equals("HomeActivity")) {
                    View mView = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityAccount", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                    ActivityHomeGroup.groupHomeGroup.replaceView(mView);
                } else if (mParentActivity.equals("AccountActivity")) {
                    View mView = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("ActivityAccount", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                    ActivityAccountGroup.groupAccountGroup.replaceView(mView);
                }
            }
        });
    }

    private void loadUsers() {
        mIsLoadingUsers = true;
        mIsLoadingNextUsers = false;
        InstagramApp.instagramApp.fetchRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users = new ArrayList<User>();
                    if (getWhat.equals(InstagramApp.GET_FOLLOWERS))
                        users = InstagramApp.instagramApp.getFollowers();
                    else if (getWhat.equals(InstagramApp.GET_FOLLOWING))
                        users = InstagramApp.instagramApp.getFollowings();

                    for (int i = 0; i < users.size(); i++) {
                        mUsers.add(users.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ActivityRelationship.this, "Check your network.", Toast.LENGTH_SHORT).show();
                }

                mIsLoadingUsers = false;
                return false;
            }
        }), getWhat, mUserId);
    }

    private void loadNextUsers() {
        mIsLoadingNextUsers = true;
        InstagramApp.instagramApp.fetchNextPageRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users = new ArrayList<User>();
                    if (getWhat.equals(InstagramApp.GET_FOLLOWERS))
                        users = InstagramApp.instagramApp.getFollowers();
                    else if (getWhat.equals(InstagramApp.GET_FOLLOWING))
                        users = InstagramApp.instagramApp.getFollowings();

                    for (int i = 0; i < users.size(); i++) mUsers.add(users.get(i));

                    adapter.notifyDataSetChanged();
                    mIsLoadingNextUsers = false;

                } else {
                    Toast.makeText(ActivityRelationship.this, "No more data.", Toast.LENGTH_SHORT).show();
                    mIsLoadingNextUsers = true;
                }
                return false;
            }
        }), getWhat);
    }

    private void loadLikedUsers() {
        mIsLoadingLikedUsers = true;
        mIsLoadingNextLikedUsers = false;
        InstagramApp.instagramApp.fetchRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users = InstagramApp.instagramApp.getLikedUsers();
                    for (int i = 0; i < users.size(); i++) {
                        mUsers.add(users.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ActivityRelationship.this, "Check your network.", Toast.LENGTH_SHORT).show();
                }

                mIsLoadingLikedUsers = false;
                return false;
            }
        }), getWhat, mUserId);
    }

    private void loadNextLikedUsers() {
        mIsLoadingNextLikedUsers = true;
        InstagramApp.instagramApp.fetchNextPageRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users = InstagramApp.instagramApp.getLikedUsers();
                    for (int i = 0; i < users.size(); i++) {
                        mUsers.add(users.get(i));
                    }

                    adapter.notifyDataSetChanged();
                    mIsLoadingNextLikedUsers = false;
                } else {
                    Toast.makeText(ActivityRelationship.this, "No more data.", Toast.LENGTH_SHORT).show();
                    mIsLoadingNextLikedUsers = true;
                }
                return false;
            }
        }), getWhat);
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
