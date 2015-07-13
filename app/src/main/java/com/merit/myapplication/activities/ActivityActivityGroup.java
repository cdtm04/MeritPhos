package com.merit.myapplication.activities;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivityActivityGroup extends ActivityGroup {
    View rootView;
    public static ActivityActivityGroup groupActivityGroup;
    private ArrayList<View> historyActivityGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.historyActivityGroup = new ArrayList<View>();
        groupActivityGroup = this;

        // Start the root activity within the group and get its view
        rootView = getLocalActivityManager().startActivity("ActivityActivity", new Intent(this, ActivityActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

        replaceView(rootView);
    }

    public void replaceView(View v) {
        try {
            historyActivityGroup.add(v);
            // Changes this Groups View to the new View
            setContentView(v);
        } catch (Exception ex) {
        }
    }


    public void back() {
        try {
            if (historyActivityGroup.size() > 1) {
                historyActivityGroup.remove(historyActivityGroup.size() - 1);
                setContentView(historyActivityGroup.get(historyActivityGroup.size() - 1));
            } else {
                //finish(); finish the current tab
                MainActivity.backToLastTab();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setRootView() {
        setContentView(rootView);
        historyActivityGroup.clear();
        historyActivityGroup.add(rootView);
    }

    @Override
    public void onBackPressed() {
        ActivityActivityGroup.groupActivityGroup.back();
    }
}
