package com.merit.myapplication.moduls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merit.myapplication.R;

import java.util.Objects;
import java.util.zip.Inflater;

/**
 * Created by merit on 7/4/2015.
 */
public class ActionBar extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private View rootView;
    private RelativeLayout buttonLeft, separator, buttonRight;
    private ImageView buttonLeftIcon, buttonRightIcon;
    private TextView labelName;

    public ActionBar(Context context) {
        super(context);
        initialize(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        setAttributeSet(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
        setAttributeSet(context, attrs);
    }

    private void initialize(Context context) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.actionbar, this, false);
        addView(rootView);

        buttonLeft = (RelativeLayout) rootView.findViewById(R.id.buttonLeft);
        buttonRight = (RelativeLayout) rootView.findViewById(R.id.buttonRight);
        separator = (RelativeLayout) rootView.findViewById(R.id.separator);
        buttonLeftIcon = (ImageView) rootView.findViewById(R.id.buttonLeftIcon);
        buttonRightIcon = (ImageView) rootView.findViewById(R.id.buttonRightIcon);
        labelName = (TextView) rootView.findViewById(R.id.labelName);

        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        // convert from header height = 50dp to pixel, to code animation
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mHeaderHeight = Math.round(50 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void setAttributeSet(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);

        // get
        String title = a.getString(R.styleable.ActionBar_header_title);
        int leftIcon = a.getResourceId(R.styleable.ActionBar_left_icon, R.drawable.icon_back);
        int rightIcon = a.getResourceId(R.styleable.ActionBar_right_icon, R.drawable.icon_option);

        // set
        if (title != null) {
            labelName.setText(title);
        }

        if (leftIcon == 0) {
            buttonLeft.setVisibility(GONE);
        } else {
            buttonLeftIcon.setImageResource(leftIcon);
            buttonLeft.setVisibility(VISIBLE);
        }

        if (rightIcon == 0) {
            buttonRight.setVisibility(GONE);
        } else {
            buttonRightIcon.setImageResource(rightIcon);
            buttonRight.setVisibility(VISIBLE);
        }

        a.recycle();
    }

    public void setLabelName(String title) {
        labelName.setText(title);
    }

    public void setButtonRightEnabled(boolean isEnabled) {
        if (isEnabled) buttonRight.setVisibility(View.VISIBLE);
        else buttonRight.setVisibility(View.GONE);
    }

    public void setButtonLeftEnabled(boolean isEnabled) {
        if (isEnabled) buttonLeft.setVisibility(View.VISIBLE);
        else buttonLeft.setVisibility(View.GONE);
    }

    public void setSeparatorEnabled(boolean isEnabled) {
        if (isEnabled) separator.setVisibility(View.VISIBLE);
        else separator.setVisibility(View.GONE);
    }

    public void setButtonLeftIcon(Object icon) {
        if (icon instanceof Drawable) buttonLeftIcon.setImageDrawable((Drawable) icon);
        else if (icon instanceof Bitmap) buttonLeftIcon.setImageBitmap((Bitmap) icon);
        else if (icon instanceof Integer) buttonLeftIcon.setImageResource((Integer) icon);
    }

    public void setButtonRightIcon(Object icon) {
        if (icon instanceof Drawable) buttonRightIcon.setImageDrawable((Drawable) icon);
        else if (icon instanceof Bitmap) buttonRightIcon.setImageBitmap((Bitmap) icon);
        else if (icon instanceof Integer) buttonRightIcon.setImageResource((Integer) icon);
    }

    public void setCameraTheme(boolean isCameraTheme) {
        if (isCameraTheme) {
            rootView.setBackgroundColor(getResources().getColor(R.color.themecamera));
            buttonLeft.setBackground(getResources().getDrawable(R.drawable.selector_press_themecamera_icon));
            buttonRight.setBackground(getResources().getDrawable(R.drawable.selector_press_themecamera_icon));
        }
    }

    // code event
    OnActionBarListener mOnActionBarListener = new OnActionBarListener() {
        @Override
        public void onButtonLeftClick(View v) {
        }

        @Override
        public void onButtonRightClick(View v) {

        }
    };

    public void setOnActionBarListener(OnActionBarListener mOnActionBarListener) {
        this.mOnActionBarListener = mOnActionBarListener;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLeft) {
            mOnActionBarListener.onButtonLeftClick(v);
        } else if (v == buttonRight) {
            mOnActionBarListener.onButtonRightClick(v);
        }
    }

    public TextView getLabelName() {
        return labelName;
    }

    public RelativeLayout getButtonLeft() {
        return buttonLeft;
    }

    public interface OnActionBarListener {
        void onButtonLeftClick(View v);

        void onButtonRightClick(View v);
    }


    // code animation

    private TranslateAnimation mTranslateAnimation;
    private boolean mVisible = true;
    private boolean mIsRunningAnimation = false;
    private int mHeaderHeight;

    public void showHeader() {
        if (mIsRunningAnimation || mVisible) {
            return;
        }

        mTranslateAnimation = new TranslateAnimation(0, 0, -mHeaderHeight, 0);
        mTranslateAnimation.setFillAfter(true);
        mTranslateAnimation.setDuration(300);
        mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsRunningAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunningAnimation = false;
                mVisible = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startAnimation(mTranslateAnimation);
    }

    public void hideHeader() {
        if (mIsRunningAnimation || !mVisible) {
            return;
        }

        mTranslateAnimation = new TranslateAnimation(0, 0, 0, -mHeaderHeight);
        mTranslateAnimation.setFillAfter(true);
        mTranslateAnimation.setDuration(300);
        mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsRunningAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunningAnimation = false;
                mVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(mTranslateAnimation);
    }
}
