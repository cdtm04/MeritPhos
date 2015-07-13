package com.merit.myapplication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import com.merit.myapplication.R;
import com.merit.myapplication.moduls.SlidingTabLayout;
import com.merit.myapplication.moduls.ViewPagerAdapter;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivityActivity extends FragmentActivity {

    SlidingTabLayout tabs;
    ViewPager pager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        initialize();
    }

    private void initialize() {
        pager = (ViewPager) findViewById(R.id.pager);

        // names of tabs
        CharSequence[] titles = {"FOLLOWING", "YOU"};
        // tabs
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentFollowing());
        fragments.add(new FragmentYou());

        // init & set adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        pager.setAdapter(adapter);

        // set view pager to sliding tabs
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // sacle full width
        tabs.setViewPager(pager);
    }

    // call back from ActivityActivityGroup, cuz when changing sliding tabs,  this sometimes calls the super.onBackPressed()
    @Override
    public void onBackPressed() {
        ActivityActivityGroup.groupActivityGroup.back();
    }
}
