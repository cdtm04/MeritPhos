package com.merit.myapplication.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.merit.myapplication.R;


public class FragmentFollowing extends Fragment {

    SwipeRefreshLayout swipeRefreshFragmentFollowing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_following, container, false);
        initialize(v);
        return v;
    }

    private void initialize(final View v) {
        swipeRefreshFragmentFollowing = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshFragmentFollowing);
        swipeRefreshFragmentFollowing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshFragmentFollowing();
                swipeRefreshFragmentFollowing.setRefreshing(false);
            }
        });
        swipeRefreshFragmentFollowing.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // CODE swipeRefreshLayout EVENT
    private void swipeRefreshFragmentFollowing() {
        // TO DO: WHEN REFRESHING LAYOUT

        Toast.makeText(getActivity(), "Loading Listview Following", Toast.LENGTH_SHORT).show();
    }
}
