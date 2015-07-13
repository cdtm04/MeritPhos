package com.merit.myapplication.activities;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivitySearchGroup extends ActivityGroup {
    View rootView;
    public static ActivitySearchGroup groupSearchGroup;
    private ArrayList<View> historySearchGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.historySearchGroup = new ArrayList<View>();
        groupSearchGroup = this;

        // Start the root activity within the group and get its view
        rootView = getLocalActivityManager().startActivity("ActivitySearch", new Intent(this, ActivitySearch.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

        replaceView(rootView);
    }

    public void replaceView(View v) {
        try {
            historySearchGroup.add(v);
            // Changes this Groups View to the new View
            setContentView(v);
        } catch (Exception ex) {
        }
    }

    public void back() {
        try {
            if (historySearchGroup.size() > 1) {
                historySearchGroup.remove(historySearchGroup.size() - 1);
                setContentView(historySearchGroup.get(historySearchGroup.size() - 1));
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
        historySearchGroup.clear();
        historySearchGroup.add(rootView);
    }

    @Override
    public void onBackPressed() {
        ActivitySearchGroup.groupSearchGroup.back();
    }
}
