package com.merit.myapplication.moduls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TabHost;

import com.merit.myapplication.activities.ActivityAccountGroup;
import com.merit.myapplication.activities.ActivityActivityGroup;
import com.merit.myapplication.activities.ActivityHomeGroup;
import com.merit.myapplication.activities.ActivitySearchGroup;
import com.merit.myapplication.activities.MainActivity;

/**
 * Created by merit on 6/28/2015.
 */
public class MainTabHost extends TabHost {
    public MainTabHost(Context context) {
        super(context);
    }

    public MainTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentTab(int index) {
        if (index == getCurrentTab()) {
            if (getCurrentTabTag().equals("t1")) {
                // the first time setcurrent tab (historyTab=1) we dont setRootView()
                if (MainActivity.historyTab.size() != 0)
                   ActivityHomeGroup.groupHomeGroup.setRootView();
            } else if (getCurrentTabTag().equals("t2")) {
                ActivitySearchGroup.groupSearchGroup.setRootView();
            } else if (getCurrentTabTag().equals("t3")) {
                ActivityActivityGroup.groupActivityGroup.setRootView();
            } else if (getCurrentTabTag().equals("t4")) {
                ActivityAccountGroup.groupAccountGroup.setRootView();
            }
        } else {
            super.setCurrentTab(index);
        }
    }
}
