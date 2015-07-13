package com.merit.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.merit.myapplication.R;
import com.merit.myapplication.moduls.ActionBar;
import com.merit.myapplication.moduls.CameraView;
import com.merit.myapplication.moduls.SlidingTabLayout;
import com.merit.myapplication.moduls.ViewPagerAdapter;

import java.util.ArrayList;


public class ActivityCamera extends FragmentActivity {
    SlidingTabLayout tabs;
    ViewPager pager;
    ViewPagerAdapter adapter;
    ActionBar abCamera;
    ImageView btnCamera;
    ArrayList<Fragment> fragments;
    FrameLayout loCamera;

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initialize();
    }

    private void initialize() {
        // init layout camera's width and height
        loCamera = (FrameLayout) findViewById(R.id.loCamera);
        Display display = getWindowManager().getDefaultDisplay();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth(), display.getWidth());
        loCamera.setLayoutParams(params);
        try {
            mCamera = Camera.open();
            Camera.Size size = mCamera.new Size(display.getWidth(), display.getWidth());
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            loCamera.addView(mCameraView);//add the SurfaceView to the layout
        }


        // init actionbar
        abCamera = (ActionBar) findViewById(R.id.abCamera);
        abCamera.setLabelName("PHOTO");
        abCamera.setButtonRightEnabled(false);
        abCamera.setCameraTheme(true);
        abCamera.setButtonLeftIcon(getResources().getDrawable(R.drawable.icon_close));


        pager = (ViewPager) findViewById(R.id.pager);

        // names of tabs
        CharSequence[] titles = {"GALLERY", "PHOTO", "VIDEO"};
        // tabs
        fragments = new ArrayList<>();
        fragments.add(new FragmentGallery());
        fragments.add(new FragmentCamera());
        fragments.add(new FragmentCamera());

        // init & set adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);

        // set view pager to sliding tabs
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // sacle full width
        tabs.setViewPager(pager);
        tabs.setSelectedIndicatorColors(Color.parseColor("#3e8fe0"));

        // init btnCamera
        btnCamera = (ImageView) findViewById(R.id.btnCamera);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0.5 && position == 1) {
                    abCamera.setLabelName("VIDEO");
                    btnCamera.setImageDrawable(ActivityCamera.this.getResources().getDrawable(R.drawable.button_record));
                } else if (positionOffset < 0.5 && position == 1) {
                    abCamera.setLabelName("PHOTO");
                    btnCamera.setImageDrawable(ActivityCamera.this.getResources().getDrawable(R.drawable.button_snap));
                } else if (positionOffset > 0.5 && position == 0) {
                    abCamera.setLabelName("PHOTO");
                } else if (positionOffset < 0.5 && position == 0) {
                    abCamera.setLabelName("GALLERY");
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class FragmentCamera extends Fragment implements View.OnTouchListener {
        RelativeLayout btnCloseCamera, loCameraOfFragment;
        ImageView btnCameraOfFragment;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_camera, container, false);
            initializeFragmentCamera(v);
            return v;
        }

        private void initializeFragmentCamera(View v) {
            btnCloseCamera = (RelativeLayout) v.findViewById(R.id.btnCloseCamera);
            loCameraOfFragment = (RelativeLayout) v.findViewById(R.id.loCameraOfFragment);
            btnCameraOfFragment = (ImageView) v.findViewById(R.id.btnCameraOfFragment);

            // set height & width for loCameraOfFragment
            Display display = getWindowManager().getDefaultDisplay();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth(), display.getWidth());
            loCameraOfFragment.setLayoutParams(params);

            btnCloseCamera.setOnTouchListener(this);
            btnCameraOfFragment.setOnTouchListener(this);
            btnCloseCamera.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v == btnCameraOfFragment && pager.getCurrentItem() == 1) {
                Drawable drawable = getResources().getDrawable(R.drawable.button_snap);
                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    drawable.setAlpha(128);
                } else {
                    drawable.setAlpha(255);
                }
                btnCamera.setImageDrawable(drawable);
            } else if (v == btnCameraOfFragment && pager.getCurrentItem() == 2) {
                Drawable drawable = getResources().getDrawable(R.drawable.button_record);
                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    drawable.setAlpha(128);
                } else {
                    drawable.setAlpha(255);
                }
                btnCamera.setImageDrawable(drawable);
            } else if (v == btnCloseCamera) {
                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    abCamera.getButtonLeft().setBackgroundColor(getResources().getColor(R.color.themecamera_pressed));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    abCamera.getButtonLeft().setBackgroundColor(getResources().getColor(R.color.themecamera));
                    finish();
                }
            }
            return false;
        }
    }

}
