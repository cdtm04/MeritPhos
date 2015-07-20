package com.merit.myapplication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.merit.myapplication.R;

/**
 * Created by merit on 7/16/2015.
 */
public class FragmentGallery extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {
    }
}
