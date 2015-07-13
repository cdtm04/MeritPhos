package com.merit.myapplication.activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

import java.util.ArrayList;

import com.merit.myapplication.R;
import com.merit.myapplication.moduls.MainTabHost;


public class MainActivity extends TabActivity {
    static MainTabHost tab;
    Drawable drawables[] = new Drawable[5];
    public static ArrayList<Integer> historyTab = new ArrayList<>();
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;
        loadDrawableIcon();
        initTabHost();
    }

    // load drawable icons to array and set alpha = 128
    private void loadDrawableIcon() {
        drawables[0] = getResources().getDrawable(R.drawable.icon_home_white);
        drawables[1] = getResources().getDrawable(R.drawable.icon_search_white);
        drawables[2] = getResources().getDrawable(R.drawable.icon_camera);
        drawables[3] = getResources().getDrawable(R.drawable.icon_news_white);
        drawables[4] = getResources().getDrawable(R.drawable.icon_profile_white);
        for (int i = 1; i < drawables.length; i++) {
            drawables[i].setAlpha(128);

        }
        // the camera icon and the home icon are still lighter
        drawables[0].setAlpha(255);
        drawables[2].setAlpha(255);

    }

    // init the TabHost
    private void initTabHost() {
        tab = (MainTabHost) findViewById(android.R.id.tabhost);
        tab.setup();
        tab.getTabWidget().setDividerDrawable(null);

        //create tabs
        TabHost.TabSpec spec1 = tab.newTabSpec("t1");
        spec1.setIndicator(null, drawables[0]);
        spec1.setContent(new Intent(MainActivity.this, ActivityHomeGroup.class));
        tab.addTab(spec1);


        TabHost.TabSpec spec2 = tab.newTabSpec("t2");
        spec2.setContent(new Intent(MainActivity.this, ActivitySearchGroup.class));
        spec2.setIndicator(null, drawables[1]);
        tab.addTab(spec2);

        TabHost.TabSpec spec0 = tab.newTabSpec("t0");
        spec0.setContent(R.id.tab0);
        spec0.setIndicator(null, drawables[2]);
        tab.addTab(spec0);

        TabHost.TabSpec spec3 = tab.newTabSpec("t3");
        spec3.setContent(new Intent(MainActivity.this, ActivityActivityGroup.class));
        spec3.setIndicator(null, drawables[3]);
        tab.addTab(spec3);

        TabHost.TabSpec spec4 = tab.newTabSpec("t4");
        spec4.setContent(new Intent(MainActivity.this, ActivityAccountGroup.class));
        spec4.setIndicator(null, drawables[4]);
        tab.addTab(spec4);

        // history size =0
        tab.setCurrentTab(0);
        // history size=1
        historyTab.add(0);

        // set background for icons
        for (int i = 0; i < tab.getTabWidget().getTabCount(); i++) {
            if (i == 2)
                tab.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_press_themecolor_icon);
            else
                tab.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_icons_background);
        }

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // start camera activity if click tab0
                if (tabId.equals("t0")) {
                    // set currenttab is the last tab
                    tab.setCurrentTab(historyTab.get(historyTab.size() - 1));
                    // after setCurrentTab, the Ontabchanged Event will be called and add new tab to history
                    // we should remove it
                    historyTab.remove(historyTab.size() - 1);
                    // start camera
                    Intent i = new Intent(MainActivity.this, ActivityCamera.class);
                    startActivity(i);
                } else {
                    // updating history
                    if (historyTab.size() <= 4) {
                        historyTab.add(tab.getCurrentTab());
                    } else {
                        historyTab.remove(1);
                        historyTab.add(tab.getCurrentTab());
                    }
                    // reset icon
                    resetDrawableIcon();
                }
            }
        });
    }

    private void resetDrawableIcon() {
        for (int i = 0; i < drawables.length; i++) {
            if (i == 2) {
            } else if (i == historyTab.get(historyTab.size() - 1))
                drawables[i].setAlpha(255);
            else drawables[i].setAlpha(128);
        }
    }

    public static void backToLastTab() {
        // remove the current tab in history
        historyTab.remove(historyTab.size() - 1);
        // set current tab is the last tab
        tab.setCurrentTab(historyTab.get(historyTab.size() - 1));

        // after setting current tab, history added a new tab (because event tab changed)
        // we should remove that new tab
        historyTab.remove(historyTab.size() - 1);

        // if value of history (at 0) = (at 1) = 0, we should remove 1 value, so it dont set tab0 again
        if (historyTab.get(0) == historyTab.get(1)) historyTab.remove(0);
    }

}
