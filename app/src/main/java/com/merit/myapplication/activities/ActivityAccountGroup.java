package com.merit.myapplication.activities;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivityAccountGroup extends ActivityGroup {
    View rootView;
    public static ActivityAccountGroup groupAccountGroup;
    private ArrayList<View> historyAccountGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.historyAccountGroup = new ArrayList<View>();
        groupAccountGroup = this;

        // Start the root activity within the group and get its view
        rootView = getLocalActivityManager().startActivity("ActivityAccount", new Intent(this, ActivityAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

        replaceView(rootView);
    }

    public void replaceView(View v) {
        try {
            historyAccountGroup.add(v);
            // Changes this Groups View to the new View
            setContentView(v);
        } catch (Exception ex) {
        }
    }

    public void back() {
        try {
            if (historyAccountGroup.size() > 1) {
                historyAccountGroup.remove(historyAccountGroup.size() - 1);
                setContentView(historyAccountGroup.get(historyAccountGroup.size() - 1));
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
        historyAccountGroup.clear();
        historyAccountGroup.add(rootView);
    }

    @Override
    public void onBackPressed() {
        ActivityAccountGroup.groupAccountGroup.back();
    }
}
