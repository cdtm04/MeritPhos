package com.merit.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.ApplicationData;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.moduls.ListViewActivityHomeAdapter;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivityHome extends Activity {
    private RelativeLayout btnDirectLayout;
    private SwipeRefreshLayout swipeRefreshHome;
    private StickyListHeadersListView lvHome;
    ListViewActivityHomeAdapter adapter;

    private ArrayList<Post> mNewFeeds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
        loadNewFeeds();
        //abc();

    }

    // init method
    private void initialize() {
        // init swipe refresh
        swipeRefreshHome = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshHome);
        swipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshHomeEvent();
                swipeRefreshHome.setRefreshing(false);
            }
        });
        swipeRefreshHome.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // init btnDirect
        btnDirectLayout = (RelativeLayout) findViewById(R.id.btnDirectLayout);
        btnDirectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityDirect.class);
                View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("ActivityDirect", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                ActivityHomeGroup.groupHomeGroup.replaceView(view);
            }
        });

        // init listview
        adapter = new ListViewActivityHomeAdapter(this, mNewFeeds);

        lvHome = (StickyListHeadersListView) findViewById(R.id.lvHome);
        lvHome.setDrawingListUnderStickyHeader(true);
        lvHome.setAreHeadersSticky(true);
        lvHome.setFastScrollEnabled(false);
        lvHome.setAdapter(adapter);
    }

    private void loadNewFeeds() {
        InstagramApp.instagramApp.fetchNewFeeds(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mNewFeeds.clear();

                    ArrayList<Post> mPosts = InstagramApp.instagramApp.getNewFeeds();

                    for (int i = 0; i < mPosts.size(); i++) mNewFeeds.add(mPosts.get(i));

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.mainContext, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }));
    }

    // CODE swipeRefreshHome EVENT
    private void swipeRefreshHomeEvent() {
        // TO DO: when sliding down the swipeRefresh
        loadNewFeeds();
    }

    @Override
    public void onBackPressed() {
        ActivityHomeGroup.groupHomeGroup.back();
    }
}
