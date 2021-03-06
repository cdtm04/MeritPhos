package com.merit.myapplication.activities;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import com.merit.myapplication.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivityHomeGroup extends ActivityGroup {
    View rootView;
    public static ActivityHomeGroup groupHomeGroup;
    private ArrayList<View> historyHomeGroup;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.historyHomeGroup = new ArrayList<View>();
        groupHomeGroup = this;

        // Start the root activity within the group and get its view
        rootView = getLocalActivityManager().startActivity("ActivityHome", new Intent(this, ActivityHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

        replaceView(rootView);
    }

    public void replaceView(View v) {
        try {
            historyHomeGroup.add(v);
            // Changes this Groups View to the new View
            setContentView(v);
        } catch (Exception ex) {
        }
    }

    public void back() {
        try {
            if (historyHomeGroup.size() > 1) {
                historyHomeGroup.remove(historyHomeGroup.size() - 1);
                setContentView(historyHomeGroup.get(historyHomeGroup.size() - 1));
            } else {
                //if history size = 1, means the last tab home in history, so the app should finish
                try {
                    MainActivity.backToLastTab();
                } catch (Exception ex) {
                    //finish();
                    if (doubleBackToExitPressedOnce) {
                        //super.onBackPressed();
                        finish();
                        return;
                    } else {
                        MainActivity.historyTab.add(0, 0);
                    }

                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setRootView() {
        setContentView(rootView);
        historyHomeGroup.clear();
        historyHomeGroup.add(rootView);
    }

    @Override
    public void onBackPressed() {
        ActivityHomeGroup.groupHomeGroup.back();
    }
}
