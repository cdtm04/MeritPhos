package com.merit.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.moduls.ActionBar;

import java.util.ArrayList;

/**
 * Created by merit on 6/25/2015.
 */
public class ActivitySearch extends Activity {
    ActionBar actionBar;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
                Toast.makeText(ActivitySearch.this, "abc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonRightClick(View v) {

            }
        });

        lv = (ListView) findViewById(R.id.lv);
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arr.add(i + "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        ActivitySearchGroup.groupSearchGroup.back();
    }
}
