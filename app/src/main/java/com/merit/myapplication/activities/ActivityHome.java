package com.merit.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.models.Caption;
import com.merit.myapplication.models.Comment;
import com.merit.myapplication.models.Location;
import com.merit.myapplication.models.Media;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.User;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
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

        // init listview home
        ArrayList<Post> postedMediaItems = new ArrayList<>();
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
        Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        postedMediaItems.add(new Post(new Location(), "", "", "ash4", new Caption(), "", "chan vai", new ArrayList<Comment>(), new ArrayList<User>(), new Media(), new User()));
        ListViewActivityHomeAdapter adapter = new ListViewActivityHomeAdapter(this, postedMediaItems);

        lvHome = (StickyListHeadersListView) findViewById(R.id.lvHome);
        lvHome.setDrawingListUnderStickyHeader(true);
        lvHome.setAreHeadersSticky(true);
        lvHome.setFastScrollEnabled(false);
        lvHome.setAdapter(adapter);
    }

    // CODE swipeRefreshHome EVENT
    private void swipeRefreshHomeEvent() {
        // TO DO: when sliding down the swipeRefresh
        Toast.makeText(ActivityHome.this, "Refeshing ListView Home", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        ActivityHomeGroup.groupHomeGroup.back();
    }
}
