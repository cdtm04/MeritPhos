package com.merit.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;

/**
 * Created by merit on 6/29/2015.
 */
public class ActivityDirect extends Activity implements View.OnClickListener {
    private RelativeLayout btnDirect, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);
        initialize();
    }

    // CODE btnDirect EVENT
    private void btnDirectEvent() {
        // TO DO: SOMETHING WHEN CLICK btnDirect

        Toast.makeText(this, "btnDirect is clicked", Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        btnDirect = (RelativeLayout) findViewById(R.id.btnDirect);
        btnDirect.setOnClickListener(this);

        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            ActivityHomeGroup.groupHomeGroup.setRootView();
        } else if (v == btnDirect) {
            btnDirectEvent();
        }
    }
}
