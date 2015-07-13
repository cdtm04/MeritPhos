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
    ActionBar abRelationship;
    SwipeRefreshLayout swipeRefreshRelationship;
    ListView lvRelationship;
    String labelName = "RELATIONSHIP";
    boolean isGetFollowers;

    ListViewActivityRelationshipAdapter adapter;
    private ArrayList<User> mUsers = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        initialize();
        loadUsers();
    }

    private void initialize() {
        // get label name from Activity Account
        Intent recieveLabelName = getIntent();
        labelName = recieveLabelName.getStringExtra("labelName");

        if (labelName.equals("FOLLOWERS")) isGetFollowers = true;
        else isGetFollowers = false;

        // init abRelationship
        abRelationship = (ActionBar) findViewById(R.id.abRelationship);
        abRelationship.setLabelName(labelName);
        abRelationship.setButtonRightEnabled(false);
        abRelationship.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
                ActivityAccountGroup.groupAccountGroup.setRootView();
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
                loadUsers();
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
                    loadNextUsers();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void loadUsers() {
        InstagramApp.instagramApp.fetchRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users;
                    if (isGetFollowers) users = InstagramApp.instagramApp.getFollowers();
                    else users = InstagramApp.instagramApp.getFollowings();

                    for (int i = 0; i < users.size(); i++) mUsers.add(users.get(i));

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ActivityRelationship.this, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }), isGetFollowers);
    }

    private void loadNextUsers() {
        InstagramApp.instagramApp.fetchNextPageRelationship(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mUsers.clear();

                    ArrayList<User> users;
                    if (isGetFollowers) users = InstagramApp.instagramApp.getFollowers();
                    else users = InstagramApp.instagramApp.getFollowings();

                    for (int i = 0; i < users.size(); i++) mUsers.add(users.get(i));

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ActivityRelationship.this, "No more data.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }), isGetFollowers);
    }
}
