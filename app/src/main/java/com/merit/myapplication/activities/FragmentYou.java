package com.merit.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import com.merit.myapplication.R;
import com.merit.myapplication.models.YouListViewItem;
import com.merit.myapplication.moduls.ListViewFragmentYouAdapter;

public class FragmentYou extends Fragment {
    SwipeRefreshLayout swipeRefreshFragmentYou;
    ListView lvYou;
    ListViewFragmentYouAdapter adapter;
    ArrayList<YouListViewItem> dataListViewYou = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_you, container, false);
        initialize(v);
        return v;
    }

    private void initialize(final View v) {
        // TO DO: INIT DATA LISTVIEW
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "ash started following you. 1h"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.pic_avatar_circle),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash. 1d"));
        dataListViewYou.add(new YouListViewItem(getResources().getDrawable(R.drawable.picture),
                getResources().getDrawable(R.drawable.button_follow),
                "Your facebook friend Ash KetChum is on Instagram as ash <font color='red'>simple</font>. 1d"));

        // init listview You
        ListView lvYou = (ListView) v.findViewById(R.id.lvYou);
        adapter = new ListViewFragmentYouAdapter(v.getContext(), dataListViewYou);
        lvYou.setAdapter(adapter);

        // init swipe refresh
        swipeRefreshFragmentYou = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshFragmentYou);
        swipeRefreshFragmentYou.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshFragmentYouEvent();
                swipeRefreshFragmentYou.setRefreshing(false);// stop refreshing
            }
        });
        swipeRefreshFragmentYou.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    // CODE swipeRefreshLayout EVENT
    private void swipeRefreshFragmentYouEvent() {
        // TO DO: WHEN REFRESHING LAYOUT

        Toast.makeText(getActivity(), "Loading Listview You", Toast.LENGTH_SHORT).show();
    }


}
